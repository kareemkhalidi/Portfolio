from c_rag.graph import nfl_article_generator

# Get user input for the article prompt
prompt = input('Enter an NFL article prompt and hit enter:\n')

# Get user input on whether to save article to a file
save = input('Save the article to a file? (y/n):\n').lower()
while save not in ['y', 'n']:
    save = input('Save the article to a file? (y/n):\n').lower()
    pass

# If yes, get user input on article name
if save == 'y':
    file = input('Name for the file (do not include .txt in the name):\n')
else:
    file = ''

# generate an article
article = 'ERROR - No article was generated.'
input = {'prompt': prompt}
output = nfl_article_generator.invoke(input)
print(output)

# print the article and save it to a file if requested
print(article)
if save == 'y':
    print(article, file=open(file + '.txt', 'w'))
