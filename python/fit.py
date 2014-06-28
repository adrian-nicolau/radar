#!/usr/bin/python

import numpy as np
import matplotlib.pyplot as plt
from scipy.optimize import curve_fit

import plotter
import parser
import constants
#import trilateration
import sys

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

x = np.array([el[0] for el in plotter.data])
y = np.array([el[1] for el in plotter.data])

distanceData = computeDistance(x, y, constants.x42, constants.y42)
RSSIdata = plotter.getRSSIs(constants.FSLMARK + ':42')
popt, pcov = curve_fit(func, distanceData, RSSIdata)
print popt
#radius = trilateration.revfunc(constants.rssi42, popt[0], popt[1], popt[2])
#print radius
"""
distanceData = computeDistance(x, y, constants.xCISCO, constants.yCISCO)
RSSIdata = plotter.getRSSIs(constants.CISCO)
popt, pcov = curve_fit(func, distanceData, RSSIdata)
print popt
radius = trilateration.revfunc(constants.rssiCISCO, popt[0], popt[1], popt[2])
print radius

distanceData = computeDistance(x, y, constants.xDLINK, constants.yDLINK)
RSSIdata = plotter.getRSSIs(constants.DLINK)
popt, pcov = curve_fit(func, distanceData, RSSIdata)
print popt
radius = trilateration.revfunc(constants.rssiDLINK, popt[0], popt[1], popt[2])
print radius

distanceData = computeDistance(x, y, constants.xNETGEAR, constants.yNETGEAR)
RSSIdata = plotter.getRSSIs(constants.NETGEAR)
popt, pcov = curve_fit(func, distanceData, RSSIdata)
print popt
radius = trilateration.revfunc(constants.rssiNETGEAR, popt[0], popt[1], popt[2])
print radius
"""

curve = [func(x, popt[0], popt[1], popt[2]) for x in distanceData]
plt.scatter(distanceData, RSSIdata)
plt.plot(distanceData, curve)
plt.show()
