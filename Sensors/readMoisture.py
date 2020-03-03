#!/usr/bin/python

import spidev
import os
import time

delay = 0.2

spi = spidev.SpiDev()
spi.open(0, 0)


def readChannel(channel):
    val = spi.xfer2([1, (8 + channel) << 4, 0])
    data = ((val[1] & 3) << 8) + val[2]
    return data


if __name__ == "__main__":
    try:
        while True:
            val = readChannel(0)
            if (val != 0):
                print(val)
            time.sleep(delay)

    except KeyboardInterrupt:
        print("Cancel.")