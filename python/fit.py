#!/usr/bin/python

import numpy as np
import matplotlib.pyplot as plt
from scipy.optimize import curve_fit

import plotter
import parser

def func(x, P0, n, d0):
    return P0 - 10 * n * np.log10(x / d0)

def computeDistance(xdata, ydata, xref, yref):
    distanceData = []
    if len(x) != len(y):
        print "Error, lens differ!"
        return
    for i in range(len(xdata)):
        distanceData.append(np.sqrt((xdata[i] - xref)**2 + (ydata[i] - yref)**2))
    return distanceData

x42 = 612
y42 = 1063

x = np.array([el[0] for el in plotter.data])
y = np.array([el[1] for el in plotter.data])
distanceData = computeDistance(x, y, x42, y42)
print distanceData

for el in distanceData:
    if el <= 0:
        print "Less than ZERO!"

RSSIdata = plotter.getRSSIs(parser.FSLMARK + ":" + "42")

plt.scatter(distanceData, RSSIdata)

popt, pcov = curve_fit(func, distanceData, RSSIdata)
print popt

curve = [func(x, popt[0], popt[1], popt[2]) for x in distanceData]

plt.plot(distanceData, curve)
plt.show()
