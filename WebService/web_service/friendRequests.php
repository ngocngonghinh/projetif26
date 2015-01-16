<?php

/*
 * List of friends
 */
require_once('../database/db.php');
require_once('../model/friend.php');
require_once('../control/valideToken.php');

$user = getUserFromToken();

if ($user !== false) {
    $friends_list = $db->search('Friend', 'friends', 'id_user2 = :id AND valide=0', array(':id' => $user->getId()));
    $list = array();
    foreach ($friends_list as $friends) {
        $list[] = $friends->toDB();
    }
    $json = array(
        'error' => false,
        'friends' => $list
    );
}

echo json_encode($json);
