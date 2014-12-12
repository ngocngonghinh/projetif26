<?php

class Contact {

    private $id;
    private $initiator;
    private $contact;
    private $message;

    function getId() {
        return $this->id;
    }

    function getInitiator() {
        return $this->initiator;
    }

    function getContact() {
        return $this->contact;
    }

    function getMessage() {
        return $this->message;
    }

    function setId($id) {
        $this->id = $id;
    }

    function setInitiator($initiator) {
        $this->initiator = $initiator;
    }

    function setContact($contact) {
        $this->contact = $contact;
    }

    function setMessage($message) {
        $this->message = $message;
    }

    public function toDB() {
        $object = get_object_vars($this);
        unset($object['message']);
        return $object;
    }

}
