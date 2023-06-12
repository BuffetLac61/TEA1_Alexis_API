<?php
include_once("libs/maLibUtils.php");
include_once("libs/modele.php");

header("Access-Control-Allow-Origin: *");
header("Access-Control-Allow-Methods: *");
header("Access-Control-Allow-Headers: *");


$data = array("version"=>1.2);
// 1.1 : ajout de la réponse à req. OPTION
// 1.2 : interdiction de supprimer/modifier pass des users 1 et 2

// Routes : /api/... pas nécessaire

$method = $_SERVER["REQUEST_METHOD"];
debug("method",$method);

if ($method == "OPTIONS") die("ok - OPTIONS");


$data["success"] = false;
$data["status"] = 400; 

// https://developers.google.com/tasks/v1/reference/

// Verif autorisation : il faut un hash
// Il peut être dans le header, ou dans la chaîne de requête

$connected = false; 

if (!($hash = valider("hash"))) 
	$hash = valider("HTTP_HASH","SERVER"); 

if($hash) {
	// Il y a un hash, il doit être correct...
	if ($connectedId = hash2id($hash)) $connected = true; 
	else {
		// non connecté - peut-être est-ce POST vers /autenticate...
		$method = "error";
		$data["status"] = 403;
	}
}


if (valider("request")) {
	$requestParts = explode('/',$_REQUEST["request"]);

	debug("rewrite-request" ,$_REQUEST["request"] ); 
	debug("#parts", count($requestParts) ); 

	$entite1 = false;
	$idEntite1 = false;
	$entite2 = false; 
	$idEntite2 = false; 

	if (count($requestParts) >0) {
		$entite1 = $requestParts[0];
		debug("entite1",$entite1); 
	} 

	if (count($requestParts) >1) {	
		if (is_id($requestParts[1])) {
			$idEntite1 = intval($requestParts[1]);
			debug("idEntite1",$idEntite1); 
		} else {
			// erreur !
			$method = "error";
			$data["status"] = 400; 
		}
	}

	if (count($requestParts) >2) {
		$entite2 = $requestParts[2];
		debug("entite2",$entite2); 
	}

	if (count($requestParts) >3) {
		if (is_id($requestParts[3])) {
			$idEntite2 = intval($requestParts[3]);
			debug("idEntite2",$idEntite2); 
		} else {
			// erreur !
			$method = "error";
			$data["status"] = 400;
		}

	}  

// TODO: en cas d'erreur : changer $method pour préparer un case 'erreur'

	$action = $method; 
	if ($entite1) $action .= "_$entite1";
	if ($entite2) $action .= "_$entite2";
 
	debug("action", $action);

	if ($action == "POST_authenticate") {
		if ($user = valider("user"))
		if ($password = valider("password")) {
			if ($hash = validerUser($user, $password)) {
				$data["hash"] = $hash;
				$data["success"] = true;
				$data["status"] = 202;
			} else {
				// connexion échouée
				$data["status"] = 401;
			}
		}
	}
	elseif ($connected)
	{
		// On connaît $connectedId
		switch ($action) {

			case 'GET_users' :			
				if ($idEntite1) {
					// GET /api/users/<id>
					$data["user"] = getUser($idEntite1);
					$data["success"] = true;
					$data["status"] = 200; 
				} 
				else {
					// GET /api/users
					$data["users"] = getUsers();
					$data["success"] = true;
					$data["status"] = 200;
				}
			break; 

			case 'GET_users_lists' : 
				if ($idEntite1)
				if ($idEntite2) {
					// GET /api/users/<id>/lists/<id>
					$data["list"] = getList($idEntite2, $idEntite1);
					$data["success"] = true;
					$data["status"] = 200;
				} else {
					// GET /api/users/<id>/lists
					$data["lists"] = getListsUser($idEntite1);
					$data["success"] = true;
					$data["status"] = 200;
				}
			break;

			case 'GET_lists' : 
				if ($idEntite1){
					// GET /api/lists/<id>
					// TODO : vérifier user ?
					$data["list"] = getList($idEntite1,$connectedId);
					$data["success"] = true;
					$data["status"] = 200;
				} else {
					// GET /api/lists
					// Les listes de l'utilisateur connecté
					$data["lists"] = getListsUser($connectedId);
					$data["success"] = true;
					$data["status"] = 200; 
				}
			break;

			case 'GET_lists_items' : 
				if ($idEntite1)
				if ($idEntite2) {
					// GET /api/lists/<id>/items/<id>
					$data["item"] = getItem($idEntite2, $idEntite1);
					$data["success"] = true;
					$data["status"] = 200;
				} else {
					// GET /api/lists<id>/items
					$data["items"] = getItems($idEntite1);
					$data["success"] = true;
					$data["status"] = 200;		 
				}
			break; 

			case 'POST_users' : 
				// POST /api/users?pseudo=&pass=...
            	// TODO : empêcher la création d'un user avec le même pseudo 
				if ($pseudo = valider("pseudo"))
				if ($pass = valider("pass")) {
                  	if (pseudoExists($pseudo)) {
                      $method = "error";
                      $data["success"] = false;
                      $data["status"] = 403;
                    } else {
                      $id = mkUser($pseudo, $pass); 
                      $data["user"] = getUser($id);
                      $data["success"] = true;
                      $data["status"] = 201;
                    }
				}
			break; 

			case 'POST_users_lists' :
				// POST /api/users/<id>/lists?label=...
				if ($idEntite1)
				if ($label = valider("label")) {
					$id = mkList($idEntite1, $label); 
					$data["list"] = getList($id);
					$data["success"] = true;
					$data["status"] = 201;
				}
			break; 

			case 'POST_lists_items' :
				// POST /api/lists/<id>/items?label=...
				if ($idEntite1)
				if ($label = valider("label")) {
					$url = valider("url"); 
					$id = mkItem($idEntite1, $label,$url);					
					$data["item"] = getItem($id,$idEntite1);
					$data["success"] = true; 
					$data["status"] = 201;
				}
			break; 

			case 'POST_lists' :
				// POST /api/lists?label=...
				if ($label = valider("label")) {
					$id = mkList($connectedId, $label); 
					$data["list"] = getList($id);
					$data["success"] = true; 
					$data["status"] = 201;
				}
			break;

			case 'PUT_authenticate' : 
				// régénère un hash ? 
				$data["hash"] = mkHash($connectedId); 
				$data["success"] = true; 
				$data["status"] = 200;
			break; 
            
            case 'PUT_users' :
				// PUT  /api/users/<id>?pass=...
            	// TODO : permette à un user de changer son mot de passe...
				
                if ($idEntite1) { 
                  // changer passe d'un user
                  if ($pass = valider("pass")) {
                      if (($idEntite1 == 1) || ($idEntite1==2)) {
                            $data["status"] = 403;
                          } else if (chgPassword($idEntite1,$pass)) {
                          $data["user"] = getUser($idEntite1);
                          $data["success"] = true; 
                          $data["status"] = 200;
                      } else {
                          // erreur 
                      }
                  }
                } else {
                	// changer passe user connecté 
                 	$method = "error";
                    $data["success"] = false;
					$data["status"] = 400;
                }
			break; 

/*
			case 'PUT_users' :
				// PUT  /api/users/<id>?pass=...
				if ($idEntite1)
				if ($pass = valider("pass")) {
                  	$data["user"] = getUser($idEntite1);
                  	if ($data["user"][]) {
                    	$method = "error";
                      	$data["success"] = false;
						$data["status"] = 401;
                    }
					else if (chgPassword($idEntite1,$pass)) {
						$data["user"] = getUser($idEntite1);
						$data["success"] = true; 
						$data["status"] = 200;
					} else {
						// erreur 
					}
                  	$method = "error";
                    $data["success"] = false;
					$data["status"] = 401;
				}
			break; 
*/

			case 'PUT_users_lists' : 
				// PUT /api/users/<id>/lists/<id>?label=...
				if ($idEntite1)
				if ($idEntite2)
				if ($label = valider("label")) {
					if (chgListLabel($idEntite2,$label,$idEntite1)) {
						$data["list"] = getList($idEntite2);
						$data["success"] = true; 
						$data["status"] = 200;
					} else {
						// erreur
					}
				}
			break; 

			case 'PUT_lists' : 
				// PUT /api/lists/<id>?label=...
				if ($idEntite1)
				if ($label = valider("label")) {
					if (chgListLabel($idEntite1,$label,$connectedId)) {
						$data["list"] = getList($idEntite1);
						$data["success"] = true; 
						$data["status"] = 200;
					} else {
						// erreur
					}
				}
			break; 

			case 'PUT_lists_items' : 
				// PUT /api/lists/<id>/items/<id>?label=...
				if ($idEntite1)
				if ($idEntite2)
				if ($label = valider("label")) {
					if (chgItemLabel($idEntite2,$label,$idEntite1)) {
						$data["item"] = getItem($idEntite2,$idEntite1);
						$data["success"] = true; 
						$data["status"] = 200;
					} else {
						// erreur
					}
				}

				// PUT /api/lists/<id>/items/<id>?url=...
				if ($idEntite1)
				if ($idEntite2)
				if ($url = valider("url")) {
					if (chgItemUrl($idEntite2,$url,$idEntite1)) {
						$data["item"] = getItem($idEntite2,$idEntite1);
						$data["success"] = true;
						$data["status"] = 200; 
					} else {
						// erreur
					}
				}

				// PUT /api/lists/<id>/items/<id>?check=0|1
				if ($idEntite1)
				if ($idEntite2){
					$check = valider("check"); 	// 0 est valide !
					if ($check !== false) {		// false, non...
						$check =intval($check); 
						if (is_check($check)) {
							if (checkItem($idEntite2,$idEntite1, $check)) {
								$data["item"] = getItem($idEntite2,$idEntite1);
								$data["success"] = true;
								$data["status"] = 200;
							} else {
								// erreur
							}
						}
					}
				}
			break;
/*
			case 'DELETE_users' : 
				// DELETE /api/users/<id> 
				if ($idEntite1) {
					if (rmUser($idEntite1)) {
						$data["success"] = true;
						$data["status"] = 200;
					} else {
						// erreur 
					} 
				}
			break; 
*/            
            case 'DELETE_users' : 
				// DELETE /api/users/<id> 
				if ($idEntite1) {
					if ($connectedId != $idEntite1) {
						$data["status"] = 403;
                      	// On ne peut supprimer un user que si on est connecté avec son identité
					} else {
                      	if (($idEntite1 == 1) || ($idEntite1==2)) {
                          $data["status"] = 403;
                        } else if (rmUser($idEntite1)) {
							$data["success"] = true;
							$data["status"] = 200;
						} else {
							// erreur 
						} 
					}
				}
			break; 

			case 'DELETE_users_lists' : 
				// DELETE /api/users/<id>/lists/<id>
				if ($idEntite1)
				if ($idEntite2) {
					if (rmList($idEntite2, $idEntite1)) {				
						$data["success"] = true;
						$data["status"] = 200; 
					} else {
						// erreur 
					}
				}
			break; 

			case 'DELETE_lists' : 
				// DELETE /api/lists/<id>
				if ($idEntite1) {
					if (rmList($idEntite1, $connectedId)) {				
						$data["success"] = true;
						$data["status"] = 200; 
					} else {
						// erreur 
					}
				}
			break; 

			case 'DELETE_lists_items' : 
				// DELETE /api/lists/<id>/items/<id>
				if ($idEntite1)
				if ($idEntite2) {
					if (rmItem($idEntite2, $idEntite1)) {				
						$data["success"] = true;
						$data["status"] = 200;  
					} else {
						// erreur 
					}
				}
			break; 
		} // switch(action)
	} //connected
}

switch($data["status"]) {
	case 200: header("HTTP/1.0 200 OK");	break;
	case 201: header("HTTP/1.0 201 Created");	break; 
	case 202: header("HTTP/1.0 202 Accepted");	break;
	case 204: header("HTTP/1.0 204 No Content");	break;
	case 400: header("HTTP/1.0 400 Bad Request");	break; 
	case 401: header("HTTP/1.0 401 Unauthorized");	break; 
	case 403: header("HTTP/1.0 403 Forbidden");	break; 
	case 404: header("HTTP/1.0 404 Not Found");		break;
	default: header("HTTP/1.0 200 OK");
		
}

echo json_encode($data);

?>
