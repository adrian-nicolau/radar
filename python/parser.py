import json
from pprint import pprint

FSLMARK = '00:17:0f:da:ba'
#fileName = "nexus4.json"
bssids = set()

def parse():
    global bssids
    fileName = raw_input("Enter file name: ")
    fp = open(fileName, 'r')
    data = json.load(fp)

    outcome = []
    noPoints = len(data.keys())
    points = ["point" + str(i) for i in range(1, noPoints + 1)]

    for name in points:

        point = data[name]

        allTogether = []
        for k in point.iterkeys():
            if k.startswith("sample"):
                for entry in point[k]:
                    allTogether.append(entry)

        allBssids = set()
        for entry in allTogether:
            allBssids.add(entry["bssid"])

        meds = []
        for bssid in allBssids:
            if bssid.startswith(FSLMARK):
                bssids.add(bssid)
                added = 0
                for entry in allTogether:
                    if entry["bssid"] == bssid:
                        added += entry["rssi"]
                meds.append((bssid, added / 5.0))

        outcome.append((point['x'], point['y'], meds))

    pprint(outcome)
    return outcome
