<?php

require_once('../model/friend.php');
require_once('../control/valideToken.php');
$user = getUserFromToken();
if ($user !== false) {
    //liste d'demandes
    $friends_list = $db->search('Friend', 'friends', 'id_user1 = :id AND valide=0', array(':id' => $user->getId()));
    $list = array();
    foreach ($friends_list as $friends) {
        $id = $friends->getId_user2();
        $friend = $db->find('User', 'user', 'id = :id', array(':id' => $id));
        if ($user !== false) {
            $list[] = $friend->simply();
        }
    }
    $json = array(
        'error' => false,
        'friends' => $list
    );
}
//}
echo json_encode($json);
