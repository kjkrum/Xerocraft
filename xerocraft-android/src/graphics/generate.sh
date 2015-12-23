#!/bin/bash

RES=../main/res

convert ./ic_launcher.png -resize 48x48 $RES/drawable-mdpi/ic_launcher.png
convert ./ic_launcher.png -resize 72x72 $RES/drawable-hdpi/ic_launcher.png
convert ./ic_launcher.png -resize 96x96 $RES/drawable-xhdpi/ic_launcher.png
convert ./ic_launcher.png -resize 144x144 $RES/drawable-xxhdpi/ic_launcher.png
convert ./ic_launcher.png -resize 192x196 $RES/drawable-xxxhdpi/ic_launcher.png

convert ./ic_notification.png -resize 24x24 $RES/drawable-mdpi/ic_notification.png
convert ./ic_notification.png -resize 36x36 $RES/drawable-hdpi/ic_notification.png
convert ./ic_notification.png -resize 48x48 $RES/drawable-xhdpi/ic_notification.png
convert ./ic_notification.png -resize 72x72 $RES/drawable-xxhdpi/ic_notification.png


