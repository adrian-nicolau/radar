import matplotlib.pyplot as plt
import numpy as np
import parser

data = parser.parse()

def getRSSIs(bssid):
    RSSIs = []
    for entry in data:
        entry_list = entry[2]
        for el in entry_list:
            if el[0] == bssid:
                RSSIs.append(el[1])
    return RSSIs

allRSSIs = [getRSSIs(bssid) for bssid in parser.bssids if bssid.startswith(parser.FSLMARK)]
Ys = [el[1] for el in data]

i = 0
for values in allRSSIs:
    label = parser.bssids.pop()
    plt.plot(Ys, values, label=label)
    i += 1

plt.xlabel('Y')
plt.ylabel('RSSI')
plt.title('Signal strength')
plt.legend()
plt.gca().invert_xaxis()
plt.show()
