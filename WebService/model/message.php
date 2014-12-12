<?php

class Message {

    private $id;
    private $contact;
    private $message;
    private $date;
    private $user;
    private $sent = false;

    function getId() {
        return $this->id;
    }

    function getContact() {
        return $this->contact;
    }

    function getMessage() {
        return $this->message;
    }

    function getDate() {
        return $this->date;
    }

    function getUser() {
        return $this->user;
    }

    function getSent() {
        return $this->sent;
    }

    function setId($id) {
        $this->id = $id;
    }

    function setContact($contact) {
        $this->contact = $contact;
    }

    function setMessage($message) {
        $this->message = $message;
    }

    function setDate($date) {
        $this->date = $date;
    }

    function setUser($user) {
        $this->user = $user;
    }

    function setSent($sent) {
        $this->sent = $sent;
    }

    public function toDB() {
        $object = get_object_vars($this);
        unset($object['sent']);
        return $object;
    }

}
