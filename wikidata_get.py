import wikipedia
import json
import sys

article = sys.argv[1]
data_wiki = wikipedia.page(article)
sections = data_wiki.sections
# print(boston_wiki.links)
content = data_wiki.content

data_dic = {}
word = ""
section_on = "preface"
data_dic["preface"] = []
for word in content.split():
   if (word not in data_dic.keys()) and (word in sections):
       section_on = word
       data_dic[section_on] = []   
   else:
       data_dic[section_on].append(word)
for key in data_dic.keys():
    data_dic[key] = ' '.join([item for item in data_dic[key]])
        
# print(data_dic["History"])
print (json.dumps(dict(boston_wiki=data_dic)))