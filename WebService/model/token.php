<?php

function isexpired($token) {
    // 7 jours; 24 heures; 60 minutes; 60 secondes
    $TIME = 7 * 24 * 60 * 60;
    $timetoken = substr($token, 32);
    $currenttime = time();
    if ($currenttime - $timetoken >= $TIME) {
        return true;
    } else {
        return false;
    }
}
