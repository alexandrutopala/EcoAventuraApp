@echo off
netsh wlan set hostednetwork mod=allow ssid=EcoServer key=ecoaventura
netsh wlan start hostednetwork
exit