/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



            function ValidateTargetValues(operators,startVals,endVals){
                var flag=false;
//                for(var j=0;j<operators.length;j++)
//                    {
//                        alert("first  "+j+"  "+startVals[j]+"  "+endVals[j]);
//                    }
                for(var i=0;i<operators.length;i++){
                    if(operators[i]=='<'||operators[i]=='<='){
                        var temp = startVals[i];
                        startVals[i] = null;
                        endVals[i] = temp;
                    }else if(operators[i]=='between'||operators[i]=='<>'){

                        if(parseInt(startVals[i])>parseInt(endVals[i])){
                            var temp = startVals[i];
                            startVals[i] = endVals[i];
                            endVals[i] = temp;
                        }
                    }else if(operators[i]=='>'||operators[i]=='>='){
                        endVals[i] = null;
                    }
                }

                var returnType;

                for(var i=0;i<operators.length-1;i++){
                   if(endVals[i]!=startVals[i+1]){
                       returnType = sortTheValues(operators,startVals,endVals);
//                       alert("returnType*********"+returnType);
                       return returnType;
                   }else if(i == operators.length-2){
                       returnType = true;
//                       alert("returnType**in else*******"+returnType)
                       return returnType;
                   }
                }
            }
            function sortTheValues(operators,startVals,endVals){
//                 for(var j=0;j<operators.length;j++)
//                    {
//                        alert("first  "+j+"  "+startVals[j]+"  "+endVals[j]);
//                    }
                var temp;
                var returnType;
                for(var i=0;i<startVals.length;i++){
                    for(var j=0;j<(startVals.length-i)-1;j++){
                        if(startVals[j+1]==null){
                            if(startVals[j]!=null){
                            temp = startVals[j];
                            startVals[j] = startVals[j+1];
                            startVals[j+1] = temp;

                            temp = endVals[j];
                            endVals[j] = endVals[j+1];
                            endVals[j+1] = temp;

                            temp = operators[j];
                            operators[j] = operators[j+1];
                            operators[j+1] = temp


                            }
                        }
                        else {
                            if(startVals[j]==null){
                            }
                            else{
                            if(parseInt(startVals[j])>parseInt(startVals[j+1])){
                            temp = startVals[j];
                            startVals[j] = startVals[j+1];
                            startVals[j+1] = temp;

                            temp = endVals[j];
                            endVals[j] = endVals[j+1];
                            endVals[j+1] = temp;

                            temp = operators[j];
                            operators[j] = operators[j+1];
                            operators[j+1] = temp
                            }
                        }
                        }
                    }
                }
                for(var i=0;i<operators.length-1;i++){
                   if(endVals[i]!=startVals[i+1]){
                       returnType = false;
                       return returnType;
                   }else if(i == operators.length-2){
                       returnType = true;
                       return returnType;
                   }
                }
            }