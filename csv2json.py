#!/usr/bin/python
#!/Library/Frameworks/Python.framework/Versions/3.6/bin/python3
import csv
import json
import sys

csvfilename = sys.argv[1]
try :
    f = open(csvfilename,'r')
except:
    print("file open failed")
    exit(1)

header = []
data = {}
    
reader = csv.reader(f)
for row in reader:
    # print(row[1])
    if len(header) > 0:
        time = ''
        datarow = {}
        for key, value in (zip(header,row)):
            if key.startswith('Year') or key.startswith('Month'):
                time = time + value
            else:
                datarow[key] = value
        datarow['time'] = time
        data[time] = datarow
        # print(datarow)
    else:
        header = row

# print (json.dumps(dict(results=results)))
print (json.dumps(dict(boston_ecoGeneralView=data)))