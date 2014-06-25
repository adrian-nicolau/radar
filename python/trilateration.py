#!/usr/bin/python

from math import sin,cos,pi
import numpy as np
import matplotlib.pyplot as plt
import constants


def revfunc(y, P0, n, d0):
    return d0 * np.power(10, (P0 - y) / (10 * n))

def drawCircle(xcenter, ycenter, radius, color='r'):
    circle = plt.Circle((xcenter, ycenter), radius)
    circle.set_edgecolor(color)
    circle.set_facecolor("none")  # "none" not None
  
    fig.gca().add_artist(circle)


ax = plt.gca()
ax.set_xlim((0, 1200))
ax.set_ylim((0, 1920))
ax.set_aspect('equal')
fig = plt.gcf()

# draw me
fig.gca().add_artist(plt.Circle((constants.xME, constants.yME), 20, color='black'))

# draw APs position
fig.gca().add_artist(plt.Circle((constants.x42, constants.y42), 3, color='black'))
fig.gca().add_artist(plt.Circle((constants.xCISCO, constants.yCISCO), 3, color='black'))
fig.gca().add_artist(plt.Circle((constants.xDLINK, constants.yDLINK), 3, color='black'))
fig.gca().add_artist(plt.Circle((constants.xNETGEAR, constants.yNETGEAR), 3, color='black'))

# draw circles - get radii from ./fit.py
drawCircle(constants.x42, constants.y42, 252, 'r')
drawCircle(constants.xCISCO, constants.yCISCO, 138, 'b')
drawCircle(constants.xDLINK, constants.yDLINK, 769, 'gray')
drawCircle(constants.xNETGEAR, constants.yNETGEAR, 365, 'g')

plt.gca().invert_yaxis()


if __name__ == '__main__':
    # show the plot
    plt.show()
