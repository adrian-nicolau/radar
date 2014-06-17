#!/usr/bin/python

import numpy as np
from scipy.optimize import curve_fit
import matplotlib.pyplot as plt
import math

def func(x, P0, n, d0):
    return P0 - 10 * n * np.log10(x / d0)

xdata = np.linspace(2, 4, 50)
y = func(xdata, 2.5, 1.3, 0.5)
ydata = y + 0.2 * np.random.normal(size=len(xdata))

popt, pcov = curve_fit(func, xdata, ydata)
print popt

ydatanew = [func(x, popt[0], popt[1], popt[2]) for x in xdata]

plt.plot(xdata, ydata)
plt.plot(xdata, ydatanew)
plt.show()
