import csv
import json


f = open("zip_code_database.csv", 'rt', encoding='latin-1')

reader = csv.reader(f)
data = {}
header = []
for row in reader:
    if len(header) > 0:
        ZipCode = row[0]
        if len(ZipCode) == 3:
            ZipCode = "00" + ZipCode
        if len(ZipCode) == 4:
            ZipCode = "0" + ZipCode
        datarow = {}
        for key, value in (zip(header,row)):
                datarow[key] = value
        data[ZipCode] = datarow
        # print(datarow)
    else:
        header = row

# print (json.dumps(dict(results=results)))
print (json.dumps(dict(ZipCodedata = data)))