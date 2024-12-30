from flask import Flask, render_template, request
from nfl_stats import NFLStats

app = Flask(__name__)
nfl = NFLStats()

@app.route('/')
def home():
    return render_template('home.html', seasons=nfl.SEASONS, teams=nfl.TEAMS)

@app.route('/submit', methods=['POST'])
def submit():
    # GET PBP FOR SELECTED SEASON
    season = int(request.form.get('season'))
    pbp = nfl.get_pbp_data(season)

    # FILTER PBP ON BASIC FILTERS
    team = request.form.get('team')
    stat_type = request.form.get('stat_type')
    side_of_ball = request.form.get('side_of_ball')

    position = request.form.get('position')
    if position == 'all':
        position = None
        season = None

    if stat_type == 'passing':
        pass_len = request.form.get('pass_len')
        if pass_len == 'all':
            pass_len = None

        pass_loc = request.form.get('pass_loc')
        if pass_loc == 'all':
            pass_loc = None

        pbp = nfl.filter_pbp_basic(pbp, team, side_of_ball, stat_type, pos=position, season=season,
                                   pass_len=pass_len, pass_loc=pass_loc)

    elif stat_type == 'rushing':
        rush_loc = request.form.get('rush_loc')
        if rush_loc == 'all':
            rush_loc = None

        rush_gap = request.form.get('rush_gap')
        if rush_gap == 'all' or rush_loc == 'middle':
            rush_gap = None

        pbp = nfl.filter_pbp_basic(pbp, team, side_of_ball, stat_type, pos=position, season=season,
                                   rush_loc=rush_loc, rush_gap=rush_gap)

    else:
        return render_template('home.html', seasons=nfl.SEASONS, teams=nfl.TEAMS)


    # FILTER PBP ON ADVANCED FILTERS (IF SEARCH TYPE IS ADVANCED)
    search_type = request.form.get('search_type')
    if search_type == 'advanced':
        downs = request.form.getlist('downs')
        if len(downs) == 4:
            downs = None
        else:
            downs = list(map(int, downs))

        yds_until_fd = request.form.get('yds_until_fd')
        if yds_until_fd == '':
            yds_until_fd = None
        else:
            yds_until_fd = int(yds_until_fd)
            if request.form.get('yds_until_fd_dir') == '<=':
                yds_until_fd = yds_until_fd * -1

        goal_to_go = request.form.get('goal_to_go')
        if goal_to_go == 'yes':
            goal_to_go = True
        elif goal_to_go == 'no':
            goal_to_go = False
        else:
            goal_to_go = None

        pt_diff = [request.form.get('pt_diff_min'), request.form.get('pt_diff_max')]
        pt_diff = list(map(int, pt_diff))
        if side_of_ball == 'defense':
            pt_diff[0], pt_diff[1] = pt_diff[1] * -1, pt_diff[0] * -1

        qtrs = request.form.getlist('qtrs')
        if len(qtrs) == 5:
            qtrs = None
        else:
            qtrs = list(map(int, qtrs))

        qtr_time_left = request.form.get('qtr_time_left')
        if qtr_time_left == '':
            qtr_time_left = None
        else:
            qtr_time_left = int(qtr_time_left)
            if request.form.get('qtr_time_left_dir') == '<=':
                qtr_time_left = qtr_time_left * -1

        temp = request.form.get('temp')
        if temp == '':
            temp = None
        else:
            temp = int(temp)
            if request.form.get('temp_dir') == '<=':
                temp = temp * -1

        wind = request.form.get('wind')
        if wind == '':
            wind = None
        else:
            wind = int(wind)
            if request.form.get('wind_dir') == '<=':
                wind = wind * -1

        pbp = nfl.filter_pbp_advanced(pbp, downs=downs, yds_until_fd=yds_until_fd, goal_to_go=goal_to_go,
                                      pt_diff=pt_diff, qtrs=qtrs, qtr_time_left=qtr_time_left, temp=temp, wind=wind)

    # RENDER AND RETURN REQUESTED HTML RESPONSE PAGE
    if stat_type == 'rushing':

        rush_stats = nfl.calc_rush_stats(pbp)
        rush_stats_list = [
            rush_stats['car'],
            rush_stats['rush_yd'],
            rush_stats['rush_td'],
            rush_stats['ypcar'],
        ]

        if rush_loc is None and rush_gap is None:
            car_chart_data = []
            ypcar_chart_data = []
            for rush_type in [['left', 'end'], ['left', 'guard'], ['left', 'tackle'], ['middle', None], ['right', 'tackle'], ['right', 'guard'], ['right', 'end']]:
                f_pbp = nfl.filter_pbp_basic(pbp, team, side_of_ball, stat_type, rush_loc=rush_type[0], rush_gap=rush_type[1])
                f_rush_stats = nfl.calc_rush_stats(f_pbp)
                car_chart_data.append(f_rush_stats['car'])
                ypcar_chart_data.append(f_rush_stats['ypcar'])
            return render_template('all_rushing_response.html', team=team,
                                   side_of_ball=(side_of_ball[0].upper() + side_of_ball[1:]),
                                   rush_stats=rush_stats_list, car_chart_data=car_chart_data,
                                   ypcar_chart_data=ypcar_chart_data)

        elif rush_loc is None and rush_gap is not None:
            car_chart_data = []
            ypcar_chart_data = []
            for rush_type in ['left', 'right']:
                f_pbp = nfl.filter_pbp_basic(pbp, team, side_of_ball, stat_type, rush_loc=rush_type)
                f_rush_stats = nfl.calc_rush_stats(f_pbp)
                car_chart_data.append(f_rush_stats['car'])
                ypcar_chart_data.append(f_rush_stats['ypcar'])
            return render_template('all_loc_rushing_response.html', team=team,
                                   side_of_ball=(side_of_ball[0].upper() + side_of_ball[1:]),
                                   rush_gap=(rush_gap[0].upper() + rush_gap[1:]), rush_stats=rush_stats_list,
                                   car_chart_data=car_chart_data, ypcar_chart_data=ypcar_chart_data)

        elif rush_loc is not None and rush_loc != 'middle' and rush_gap is None:
            car_chart_data = []
            ypcar_chart_data = []
            for rush_type in ['end', 'guard', 'tackle']:
                f_pbp = nfl.filter_pbp_basic(pbp, team, side_of_ball, stat_type, rush_gap=rush_type)
                f_rush_stats = nfl.calc_rush_stats(f_pbp)
                car_chart_data.append(f_rush_stats['car'])
                ypcar_chart_data.append(f_rush_stats['ypcar'])
            return render_template('all_gap_rushing_response.html', team=team,
                                   side_of_ball=(side_of_ball[0].upper() + side_of_ball[1:]),
                                   rush_loc=(rush_loc[0].upper() + rush_loc[1:]), rush_stats=rush_stats_list,
                                   car_chart_data=car_chart_data, ypcar_chart_data=ypcar_chart_data)

        else:
            if rush_loc == 'middle':
                return render_template('filtered_rushing_response.html', team=team,
                                       side_of_ball=(side_of_ball[0].upper() + side_of_ball[1:]),
                                       rush_loc='Middle of Field',
                                       rush_gap='',
                                       rush_stats=rush_stats_list)
            else:
                return render_template('filtered_rushing_response.html', team=team,
                                       side_of_ball=(side_of_ball[0].upper() + side_of_ball[1:]),
                                       rush_loc=(rush_loc[0].upper() + rush_loc[1:] + ' '),
                                       rush_gap=(rush_gap[0].upper() + rush_gap[1:]),
                                       rush_stats=rush_stats_list)

    elif stat_type == 'passing':

        pass_stats = nfl.calc_pass_stats(pbp)
        pass_stats_list = [
            pass_stats['att'],
            pass_stats['cmp'],
            pass_stats['pass_yd'],
            pass_stats['pass_td'],
            pass_stats['int'],
            pass_stats['ypatt'],
            pass_stats['ypcmp'],
        ]

        if pass_len is None and pass_loc is None:
            att_chart_data = []
            ypatt_chart_data = []
            cmp_chart_data = []
            ypcmp_chart_data = []
            for pass_type in [['short', 'left'], ['short', 'middle'], ['short', 'right'], ['deep', 'left'], ['deep', 'middle'], ['deep', 'right']]:
                f_pbp = nfl.filter_pbp_basic(pbp, team, side_of_ball, stat_type, pass_len=pass_type[0], pass_loc=pass_type[1])
                f_pass_stats = nfl.calc_pass_stats(f_pbp)
                att_chart_data.append(f_pass_stats['att'])
                ypatt_chart_data.append(f_pass_stats['ypatt'])
                cmp_chart_data.append(f_pass_stats['cmp'])
                ypcmp_chart_data.append(f_pass_stats['ypcmp'])
            return render_template('all_passing_response.html', team=team,
                                   side_of_ball=(side_of_ball[0].upper() + side_of_ball[1:]),
                                   pass_stats=pass_stats_list, att_chart_data=att_chart_data,
                                   ypatt_chart_data=ypatt_chart_data, cmp_chart_data=cmp_chart_data,
                                   ypcmp_chart_data=ypcmp_chart_data)

        elif pass_len is None and pass_loc is not None:
            att_chart_data = []
            ypatt_chart_data = []
            cmp_chart_data = []
            ypcmp_chart_data = []
            for pass_type in ['short', 'deep']:
                f_pbp = nfl.filter_pbp_basic(pbp, team, side_of_ball, stat_type, pass_len=pass_type)
                f_pass_stats = nfl.calc_pass_stats(f_pbp)
                att_chart_data.append(f_pass_stats['att'])
                ypatt_chart_data.append(f_pass_stats['ypatt'])
                cmp_chart_data.append(f_pass_stats['cmp'])
                ypcmp_chart_data.append(f_pass_stats['ypcmp'])
            return render_template('all_len_passing_response.html', team=team,
                                   side_of_ball=(side_of_ball[0].upper() + side_of_ball[1:]),
                                   pass_loc=(pass_loc[0].upper() + pass_loc[1:]),
                                   pass_stats=pass_stats_list, att_chart_data=att_chart_data,
                                   ypatt_chart_data=ypatt_chart_data, cmp_chart_data=cmp_chart_data,
                                   ypcmp_chart_data=ypcmp_chart_data)

        elif pass_len is not None and pass_loc is None:
            att_chart_data = []
            ypatt_chart_data = []
            cmp_chart_data = []
            ypcmp_chart_data = []
            for pass_type in ['left', 'middle', 'right']:
                f_pbp = nfl.filter_pbp_basic(pbp, team, side_of_ball, stat_type, pass_loc=pass_type)
                f_pass_stats = nfl.calc_pass_stats(f_pbp)
                att_chart_data.append(f_pass_stats['att'])
                ypatt_chart_data.append(f_pass_stats['ypatt'])
                cmp_chart_data.append(f_pass_stats['cmp'])
                ypcmp_chart_data.append(f_pass_stats['ypcmp'])
            return render_template('all_loc_passing_response.html', team=team,
                                   side_of_ball=(side_of_ball[0].upper() + side_of_ball[1:]),
                                   pass_len=(pass_len[0].upper() + pass_len[1:]),
                                   pass_stats=pass_stats_list, att_chart_data=att_chart_data,
                                   ypatt_chart_data=ypatt_chart_data, cmp_chart_data=cmp_chart_data,
                                   ypcmp_chart_data=ypcmp_chart_data)

        else:
            return render_template('filtered_passing_response.html', team=team,
                                   side_of_ball=(side_of_ball[0].upper() + side_of_ball[1:]),
                                   pass_len=(pass_len[0].upper() + pass_len[1:]),
                                   pass_loc=(pass_loc[0].upper() + pass_loc[1:]),
                                   pass_stats=pass_stats_list)

    else:
        return render_template('home.html', seasons=nfl.SEASONS, teams=nfl.TEAMS)

if __name__ == '__main__':
    # app.run(debug=True)
    app.run()
