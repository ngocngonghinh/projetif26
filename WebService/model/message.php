<?php
class Message
{
	public $id;
	public $contact;
	public $message;
	public $date;
	public $user;

	public $sent = false;

	public function toDB()
	{
		$object = get_object_vars($this);
		unset($object['sent']);
		return $object;
	}
}