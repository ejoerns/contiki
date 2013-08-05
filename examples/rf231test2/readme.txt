This application provides testcases for throughput measurements of
the AES implementation on a uIP IPv6 basis.

to build the receiver:

goto .\receiver
call  'make TARGET=avr-raven'


to build the sender:

goto .\sender
call  'make TARGET=avr-raven DEFINES=AES=n'
                where n=0..3 Defines the AES Implementation used
