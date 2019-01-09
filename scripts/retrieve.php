<?php
    session_start();
    require "connection.php";

    $product_id = $_POST['product_id'];
    
    $sql = "SELECT * FROM product WHERE ProductID=:product_id";
    $query = $conn->prepare($sql);  
    $query->bindParam(':product_id', $product_id);
    $query->execute();
    $result = $query->fetch();
    $query->closeCursor();

    $arrCol = array();
    $response = array();

        $tmp = array();
        $tmp['product_id'] = $result["ProductID"];
        $tmp['name'] = $result["Name"];
        $tmp['price'] = $result["Price"];
        $tmp['quantity'] = $result["Quantity"];
        array_push($response, $tmp);
    
        
    echo json_encode($response);


    $product_id = $_POST["product_id"];
    $name = $_POST["name"];
    $return_date = date('Y-m-d');
    $return_time = time('hh:mm:ss');
    $comment = $_POST["comment"];
    $sale_id = $_POST["sale_id"];
   
   
   
    $sql = "INSERT INTO return_table(ProductID, Name ReturnDate, ReturnTime, Comment, SaleID) 
            VALUES(:product_id, :name, :return_date, :return_time, :comment, :sale_id)";
    $stmt = $conn->prepare($sql);
    $stmt->bindParam(':product_id', $product_id);
    $stmt->bindParam(':name', $name);
    $stmt->bindParam(':return_date', $return_date);
    $stmt->bindParam(':return_time', $return_time);
    $stmt->bindParam(':comment', $comment);
    $stmt->bindParam(':sale_id', $sale_id);
    $result = $stmt->execute();

    if(!$result)
    {
        $code = "return_failed";
        $message = "Error";
        array_push($response, array("code"=>$code, "message"=>$message));
        echo json_encode($response);
    }
    elseif ($result) 
    {
        $code = "return_success";
        $message = "Successfully";
        array_push($response, array("code"=>$code, "message"=>$message));
        echo json_encode($response);
    }
    
?>