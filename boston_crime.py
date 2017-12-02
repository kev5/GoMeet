import csv
import json


f1 = open("crime_boston.csv", 'rt', encoding='latin-1')
reader1 = csv.reader(f1)
f2 = open("boston_zipcode.csv", 'rt', encoding='UTF-8')
reader2 = csv.reader(f2)
f3 = open("zip_pos_us.csv", 'rt', encoding = 'latin-1')
reader3 = csv.reader(f3)

zip_boston = []
zip_boston_pos = {}

for row in reader2:
    zip_boston.append('0' + row[0].split('\t')[0])
    zip_boston_pos[('0' + row[0].split('\t')[0])] = row[0].split('\t')[1], row[0].split('\t')[2]
# print(zip_boston)

for row in reader3:#for more accurate positions
    if row[0] in zip_boston:
        zip_boston_pos[row[0]] = (row[1], row[2])
# print(zip_boston_pos)

def findzip(lat, log):
    lat = float(lat)
    log = float(log)
    lenmin = 10000000000000.0
    zipcode = ""
    for key,value in zip_boston_pos.items():
        distance = (lat - float(value[0]))**2 + (log - float(value[1]))**2
        if ( distance < lenmin ):
            zipcode  = key
            lenmin = distance
    return zipcode
        
data = {}
header = []
zipcode = ""
for row in reader1:
    datarow = {}
    if len(header) > 0:
        try:
            zipcode = findzip(float(row[14]), float(row[15]))
            row.append(zipcode)
        except:
            continue        
        for key, value in (zip(header,row)):
                datarow[key] = value
        if zipcode not in data.keys():
            data[zipcode] = {}
        else:
            data[zipcode][row[0]] = datarow
    else:
        header = row
        header.append('zipcode')

crime_stat = {}
for zipcode, d in data.items():
    crime_stat[zipcode] = {}
    crime_stat[zipcode]["count"] = len(d.keys())
    crime_stat[zipcode]["description"] = ""
    crime_cata = {}
    mostOccurredCrime = ""
    crimecataCount = 0
    crime_str = {}
    mostOccurredStr = ""
    crimestrCount = 0
    for key,value in d.items():
        if value["OFFENSE_DESCRIPTION"] not in crime_cata:
            crime_cata[value["OFFENSE_DESCRIPTION"]] = 0
        else: 
            crime_cata[value["OFFENSE_DESCRIPTION"]] += 1
        if crime_cata[value["OFFENSE_DESCRIPTION"]] > crimecataCount:
            mostOccurredCrime = value["OFFENSE_DESCRIPTION"]
            crimecataCount = crime_cata[value["OFFENSE_DESCRIPTION"]]           
        if value["STREET"] not in crime_str:
            crime_str[value["STREET"]] = 0
        else: 
            crime_str[value["STREET"]] += 1
        if crime_str[value["STREET"]] > crimestrCount:
            mostOccurredStr = value["STREET"]
            crimestrCount = crime_str[value["STREET"]]
    crime_stat[zipcode]["mostOccurredCrime"] = mostOccurredCrime
    crime_stat[zipcode]["mostOccurredStr"] = mostOccurredStr
    crime_stat[zipcode]["description"] = "from 2015, " + str(crime_stat[zipcode]["count"]) + " crimes happed in this area, the issue most frequently happend is " + mostOccurredCrime + " and the street " + mostOccurredStr + " is where things happens the most."
# print(crime_stat)
        
    
# print(data['02210'])
json.dumps(dict(boston_crime_data_from2015 = data))
print(json.dumps(dict(boston_crime_data_from2015_statistics = crime_stat)))