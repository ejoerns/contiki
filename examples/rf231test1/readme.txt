This application provides testcases for speed measurements of the AES implementation.
This is done with a minimalistic avr-raven contiki.

to build:

call  'make TARGET=avr-raven DEFINES=AES=n'
                where n=0..3 Defines the AES Implementation used
