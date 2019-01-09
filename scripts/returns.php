<?php
    session_start();
    require "connection.php";

    date_default_timezone_set('Africa/Johannesburg');

    $return_date = date('Y-m-d');
    $return_time = date("H:i:s");  
    $comment = $_POST["comment"];
    $sales_id = $_POST["sales_id"];

    $quantity_list = $_POST['quantity_list'];
    $quantity_list = stripcslashes($quantity_list);
    $objA = json_decode($quantity_list, true);
    
    if ($sales_id != null) 
    {
       $sql = "INSERT INTO return_table(ReturnDate, ReturnTime, Comment, SaleID) 
                VALUES(:return_date, :return_time, :comment, :sales_id)";

        $stmt = $conn->prepare($sql);
        $stmt->bindParam(':return_date', $return_date);
        $stmt->bindParam(':return_time', $return_time);
        $stmt->bindParam(':comment', $comment);
        $stmt->bindParam(':sales_id', $sales_id);
        $stmt->execute();

        if($stmt)
        {
            $sql1 ="SELECT ReturnID FROM return_table 
                    WHERE ReturnDate =:return_date 
                    AND ReturnTime =:return_time 
                    AND Comment =:comment 
                    AND SaleID =:sales_id";

            $stmt1 = $conn->prepare($sql1); 
            $stmt1->bindParam(':return_date', $return_date);
            $stmt1->bindParam(':return_time', $return_time);
            $stmt1->bindParam(':comment', $comment);
            $stmt1->bindParam(':sales_id', $sales_id);
            $stmt1->execute();
            $result = $stmt1->fetch();
            $stmt1->closeCursor();
            $arrCol = array();
            $response = array();
       
            $return_id = $result["ReturnID"];
        }
    }

    if($return_id != null && $objA != null)
    {

        foreach ($objA as $row)
        {
            $sql1 = "INSERT INTO product_return(ReturnID, ProductID, Quantity) VALUES(:return_id, :product_id, :quantity)";
            $stmt1 = $conn->prepare($sql1);
            $stmt1->bindParam(':return_id', $return_id);
            $stmt1->bindParam(':product_id', $row['product_id']);
            $stmt1->bindParam(':quantity', $row['quantity']);
            $stmt1->execute();
            $stmt1->closeCursor();


            foreach ($objA as $row)
            {
                $sql2 ="UPDATE product 
                        SET Quantity = Quantity + :quantity 
                        WHERE ProductID = :product_id";

                $stmt2 = $conn->prepare($sql2);
                $stmt2->bindParam(':product_id', $product_id);
                $stmt2->bindParam(':quantity', $quantity);
                $stmt2->execute();
                $stmt2->closeCursor();
            }
        }

        $code = "return_success";
        $message = "Return Successfully";
        array_push($response, array("code"=>$code, "message"=>$message));
    }
   
    else
    {
        $code = "return_failed";
            $message = "Return Unsuccessfully";
            array_push($response, array("code"=>$code, "message"=>$message));
    }
    
    echo json_encode($response);



?>


