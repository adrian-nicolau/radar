import json
from pprint import pprint

FSLMARK = '00:17:0f:da:ba'
CISCO = "48:f8:b3:4e:79:c1"
DLINK = "c8:d3:a3:06:6e:aa"
NETGEAR = "00:90:4c:7e:00:6e"

bssids = set()

def parse():
    global bssids
    #fileName = raw_input("Enter file name: ")
    fp = open('final.json', 'r')
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
            if bssid.startswith(FSLMARK) or bssid.startswith(CISCO) or bssid.startswith(DLINK) or bssid.startswith(NETGEAR):
                bssids.add(bssid)
                added = 0
                ct = 0.0
                for entry in allTogether:
                    if entry["bssid"] == bssid:
                        added += entry["rssi"]
                        ct += 1
                if ct == 0:
                    ct = 1
                meds.append((bssid, added / ct))

        outcome.append((point['x'], point['y'], meds))

    pprint(outcome)
    return outcome
