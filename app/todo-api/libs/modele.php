<?php
include_once("maLibSQL.pdo.php"); 
// définit les fonctions SQLSelect, SQLUpdate...


// Users ///////////////////////////////////////////////////

function validerUser($pseudo, $pass){
	$SQL = "SELECT id FROM todo_users WHERE pseudo='$pseudo' AND pass='$pass'";
	if ($id=SQLGetChamp($SQL))
		return getHash($id);
	else return false;
}

function pseudoExists($pseudo){
	$SQL = "SELECT id FROM todo_users WHERE pseudo='$pseudo'";
	return SQLGetChamp($SQL); 
}

function hash2id($hash) {
	$SQL = "SELECT id FROM todo_users WHERE hash='$hash'";
	return SQLGetChamp($SQL); 
}

function hash2pseudo($hash) {
	$SQL = "SELECT pseudo FROM todo_users WHERE hash='$hash'";
	return SQLGetChamp($SQL); 
}

function getUsers(){
	$SQL = "SELECT id,pseudo FROM todo_users";
	return parcoursRs(SQLSelect($SQL));
}

function getUser($idUser){
	$SQL = "SELECT id,pseudo FROM todo_users WHERE id='$idUser'";
	$rs = parcoursRs(SQLSelect($SQL));
	if (count($rs)) return $rs[0]; 
	else return array();
}

function getHash($idUser){
	$SQL = "SELECT hash FROM todo_users WHERE id='$idUser'";
	return SQLGetChamp($SQL);
}

function mkHash($idUser) {
	// génère un (nouveau) hash pour cet user
	// il faudrait ajouter une date d'expiration
	$dataUser = getUser($idUser);
	if (count($dataUser) == 0) return false;
 
	$payload = $dataUser["pseudo"] . date("H:i:s");
	$hash = md5($payload); 
	$SQL = "UPDATE todo_users SET hash='$hash' WHERE id='$idUser'"; 
	SQLUpdate($SQL); 
	return $hash; 
}

function mkUser($pseudo, $pass){
	$SQL = "INSERT INTO todo_users(pseudo,pass) VALUES('$pseudo', '$pass')";
	$idUser = SQLInsert($SQL);
	mkHash($idUser); 
	return $idUser; 
}


function rmUser($idUser) {
	$SQL = "DELETE FROM todo_users WHERE id='$idUser'";
	return SQLDelete($SQL);
}

function chgPassword($idUser,$pass) {
	$SQL = "UPDATE todo_users SET pass='$pass' WHERE id='$idUser'";
	return SQLUpdate($SQL);
}

// Lists ///////////////////////////////////////////////////


function getLists(){
	$SQL = "SELECT l.id, l.label, u.pseudo FROM todo_lists l INNER JOIN todo_users u ON l.idUser = u.id"; 
	return parcoursRs(SQLSelect($SQL));
}

function getList($id,$idUser=false){
	$SQL = "SELECT id,label FROM todo_lists WHERE id='$id'"; 
	if ($idUser)
		$SQL .= " AND idUser='$idUser'"; 
	$rs = parcoursRs(SQLSelect($SQL));
	if (count($rs)) return $rs[0]; 
	else return array();
}

function getListsUser($idUser){
	$SQL = "SELECT id,label FROM todo_lists WHERE idUser='$idUser'"; 
	return parcoursRs(SQLSelect($SQL));
}

function mkList($idUser, $label){
	$SQL = "INSERT INTO todo_lists(idUser,label) VALUES('$idUser', '$label')";
	return SQLInsert($SQL);
}

function rmList($idList, $idUser=false) {
	$SQL = "DELETE FROM todo_lists WHERE id='$idList'";
	if ($idUser) $SQL .= " AND idUser='$idUser'"; 
	return SQLDelete($SQL);
}

function chgListLabel($idList,$label, $idUser=false) {
	$SQL = "UPDATE todo_lists SET label='$label' WHERE id='$idList'";
	if ($idUser) $SQL .= " AND idUser='$idUser'";
	return SQLUpdate($SQL);
}

// Items ///////////////////////////////////////////////////

function getItems($idList) {
	$SQL = "SELECT id,label,url,checked FROM todo_items WHERE idList='$idList'"; 
	return parcoursRs(SQLSelect($SQL));
}

function getItem($idItem, $idList=false) {
	$SQL = "SELECT id,label,checked,url FROM todo_items WHERE id='$idItem'"; 
	if ($idList) $SQL .= " AND idList='$idList'";

	$rs = parcoursRs(SQLSelect($SQL));
	if (count($rs)) return $rs[0]; 
	else return array();
}


function rmItem($idItem,$idList) {
	$SQL = "DELETE FROM todo_items WHERE id='$idItem' AND idList='$idList'";
	return SQLDelete($SQL);
}

function mkItem($idList, $label,$url=false){
	if ($url)
		$SQL = "INSERT INTO todo_items(idList,label,url) VALUES('$idList', '$label','$url')";
	else 
		$SQL = "INSERT INTO todo_items(idList,label) VALUES('$idList', '$label')";
	return SQLInsert($SQL);
}

function chgItemLabel($idItem,$label,$idList=false) {
	$SQL = "UPDATE todo_items SET label='$label' WHERE id='$idItem'";
	if ($idList) $SQL .=  " AND idList='$idList'"; 
	return SQLUpdate($SQL);
}

function chgItemUrl($idItem,$url,$idList=false) {
	$SQL = "UPDATE todo_items SET url='$url' WHERE id='$idItem'";
	if ($idList) $SQL .=  " AND idList='$idList'"; 
	return SQLUpdate($SQL);
}

function checkItem($idItem, $idList, $checkValue=1) {
	$SQL = "UPDATE todo_items SET checked='$checkValue' WHERE id='$idItem' AND idList='$idList'";
	return SQLUpdate($SQL);
}



?>
