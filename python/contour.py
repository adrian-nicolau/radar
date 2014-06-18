import numpy as np
import matplotlib.pyplot as plt
import scipy.interpolate
from operator import itemgetter

import parser
import plotter

# Establish data

macEnd = raw_input("Please enter last byte of MAC address: ")

x = np.array([el[0] for el in plotter.data])
y = np.array([el[1] for el in plotter.data])
z = np.array(plotter.getRSSIs(parser.FSLMARK + ":" + macEnd))

# Set up a regular grid of interpolation points
xi, yi = np.linspace(x.min(), x.max(), 100), np.linspace(y.min(), y.max(), 100)
xi, yi = np.meshgrid(xi, yi)

# Interpolate
rbf = scipy.interpolate.Rbf(x, y, z, function='linear')
zi = rbf(xi, yi)

plt.imshow(zi, vmin=z.min(), vmax=z.max(), origin='lower', 
            extent=[x.min(), x.max(), y.min(), y.max()])
plt.gca().invert_yaxis()
plt.scatter(x, y, c=z)
plt.colorbar()
plt.show()
