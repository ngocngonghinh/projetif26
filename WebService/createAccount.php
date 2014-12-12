<?php

require_once('database/db.php');
require_once('model/user.php');
$parameters = array
    (
    ':token' => null
);
foreach ($_GET as $key => $value) {
    $parameters[":$key"] = $value;
}

$json = array(
    'error' => true
);
$config = require_once('config.php');
$db = new DB($config['dsn'], $config['username'], $config['password'], $config['options']);

/**
 * Check user is existed or not
 */
function isUserExisted($email) {
    global $db;
    $user = $db->find('User', 'user', 'email = :email', array(':email' => $email));
    if ($user !== false)
        return true;
    else
        return false;
}

/**
 * Storing new user
 * returns user details
 */
function storeUser($name, $email, $password) {
    global $db;
    global $json;
    $hash = hashSSHA($password);
    $encrypted_password = $hash["encrypted"]; // encrypted password
    $salt = $hash["salt"]; // salt
    $token = md5(time() . $email . $password);
    $paras = array('name' => $name, "email" => $email, "password" => $encrypted_password, "salt" => $salt, "token" => $token);
    $user = $db->insertSQL($paras, 'user');
    //var_dump($user);
    // check for successful store
    if ($user !== false) {
        $json = array(
            'error' => false,
            'token' => $token
        );
    }
}

/**
 * Encrypting password
 * @param password
 * returns salt and encrypted password
 */
function hashSSHA($password) {

    $salt = sha1(rand());
    $salt = substr($salt, 0, 10);
    //$hash = hash('sha256', $password);
    $encrypted = hash('sha256', hash('sha256', $password . $salt) . $salt);
    $hash = array("salt" => $salt, "encrypted" => $encrypted);
    return $hash;
}

if (!isUserExisted($parameters[":email"])) {
    storeUser($parameters[":name"], $parameters[":email"], $parameters[":password"]);
}
echo json_encode($json);
