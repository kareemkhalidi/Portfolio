import sys
import subprocess

'''
Takes the provided URL(s) and ingests/embeds it/them into 
a vectorstore database for use by the C-RAG when making 
generations. Think of it as providing a source for the C-RAG to use.

Usage:
  python ingest_urls.py [URL(s)] [optional parameters]
  If an optional parameter is passed multiple times, the last value
  will be used. Optional parameters can be passed in any order.

Parameters:
  [URL(s)]: The URL(s) to ingest.
    Must be the first parameter when calling the script. To pass a single URL
    simply pass the URL as the first parameter. To pass a list of URLs, pass
    the name of a .txt file that has one URL on each line and no other text.

  -s[chunk size (optional)]: The size that each chunk should be when splitting the URL.
    Optional, defaults to 250 if no value provided. Must be preceded with -s 
    (for example: -s300 to pass a chunk size of 300).

  -o[chunk overlap (optional)]: The chunk size to use when splitting the URL.
    Optional, defaults to 50 if no value provided. Must be preceded with -o 
    (for example: -o75 to pass a chunk overlap of 75).
'''

# constants
DEFAULT_CHUNK_SIZE = 250
DEFAULT_CHUNK_OVERLAP = 50

# if not enough parameters provided, quit
if len(sys.argv) < 2:
    print('ERROR - No parameters provided, at least one is expected.'
          'Call "python ingest_urls.py help" to see the help menu.')
    quit()

# if first parameter is 'help', print help menu and quit
if sys.argv[1].lower() == 'help':
    print(open('ingest_urls_help.txt', 'r').read())
    quit()

# load urls from input file (or just add url to a list)
if sys.argv[1].endswith('.txt'):
    urls = open(sys.argv[1], 'r').readlines()
    for i in range(len(urls)):
        urls[i] = urls[i].strip()
else:
    urls = [sys.argv[1]]

# extract optional parameter values (or set to default values if not provided)
chunk_size = DEFAULT_CHUNK_SIZE
chunk_overlap = DEFAULT_CHUNK_OVERLAP
for param in sys.argv:
    if param.startswith('-s'):
        try:
            chunk_size = int(param[2:])
        except:
            print('ERROR - An integer value must be provided for the -s parameter.')
            quit()
    if param.startswith("-o"):
        try:
            chunk_overlap = int(param[2:])
        except:
            print('ERROR - An integer value must be provided for the -o parameter.')
            quit()

# run the ingest_urls_helper.py script to (attempt to) ingest each url
cur = 1
for url in urls:
    print()
    print()
    print('LOADING URL PAGE CONTENT AND ADDING TO VECTORSTORE... ({cur}/{total})'
            .format(cur=cur, total=len(urls)))
    command = ('python ingest_urls_helper.py {url} {chunk_size} {chunk_overlap}'
                .format(url=url, chunk_size=chunk_size, chunk_overlap=chunk_overlap))
    subprocess.run(command)
    cur += 1
