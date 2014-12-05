<?php
class Contact
{
	public $id;
	public $initiator;
	public $contact;

	public $message;

	public function toDB()
	{
		$object = get_object_vars($this);
		unset($object['message']);
		return $object;
	}
}