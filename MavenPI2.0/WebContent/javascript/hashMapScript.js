/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */


function HashMap(){

    var ItemCollection=new Array();
    var keyArray=new Array();
    //private

    this.getMapCollection=function (){
        return ItemCollection;
    }

    var MapItem=function (key,value){

        this.key=(key==null?"":key);
        this.value=(value==null?"":value);

        this.getKey=function (){
            return this.key;
        }

        this.getValue=function (){
            return this.value;
        }

        this.setKey=function (key){
            this.key=key;
        }

        this.setValue=function (value){
            this.value=value;
        }
    }

    this.put=function (key,value){
        var item=new MapItem();
        if(isItemExist(key)==false)
        {
            item.setKey(key);
            item.setValue(value);
            ItemCollection[ItemCollection.length]=item;
        }else {
            item=getItem(key);
            item.setValue(value);
        }
    }



    this.putAll=function (itemCollection){
        if (HashMap.prototype.isPrototypeOf(itemCollection))
        {
            connectMap(itemCollection.getMapCollection());

            return true;

        } else {
            if (Array.prototype.isPrototypeOf(itemCollection))
            {
                for (var j=0;j<itemCollection.length ;j++ )
                {
                    if (MapItem.prototype.isPrototypeOf(itemCollection[j])==false)
                    {
                        return false;
                    }
                }

                connectMap(itemCollection);

                return true;
            }
        }
    }

    var connectMap=function (itemsArray){
        if (ItemCollection.length==0)
        {
            for (var i=0;i<itemsArray.length;i++)
            {
                ItemCollection[i]=itemsArray[i];
            }
        } else {
            var j= (parseInt(itemsArray.length)+parseInt(ItemCollection.length));
            var orginalLength=parseInt(ItemCollection.length);                //save the length of ItemCollection before putting

            for (var i=parseInt(ItemCollection.length) ; i<j; i++ )
            {
                ItemCollection[i]=itemsArray[i-orginalLength];
            }
        }
    }

    var getItem=function (key){
        var item=new MapItem();

        for(var i=0;i<ItemCollection.length;i++)
        {
            if(ItemCollection[i].getKey()==key)
            {
                item=ItemCollection[i];
                break;
            }
        }
        return item;
    }

    this.get=function (key){

        var value="";

        for(var i=0;i<ItemCollection.length;i++)
        {
            if(ItemCollection[i].getKey()==key)
            {
                value=ItemCollection[i].getValue();
                break;
            }
        }
        return value;

    }

    var isItemExist=function (key){

        var item=new MapItem();
        item.setKey("");
        item.setValue("");
        var flag=false;

        for(var i=0;i<ItemCollection.length;i++)
        {

            if(ItemCollection[i].getKey()==key)
            {
                flag=true;
                break;
            }

        }

        return flag;

    }

    this.remove=function (key){
        for (var i=0;i<ItemCollection.length;i++)
        {

            if (ItemCollection[i].getKey()==key)
            {
                var mid=ItemCollection.length/2;

                if (i<mid)
                {
                    for (var j=i;j>0 ;j-- )
                    {
                        ItemCollection[j]=ItemCollection[j-1];
                    }
                } else {
                    for (var j=i;j<ItemCollection.length ;j++ )
                    {
                        ItemCollection[j]=ItemCollection[j+1];
                    }
                }
                ItemCollection.length=ItemCollection.length-1;
            }
        }
    }

    this.removeAll=function (){
        ItemCollection.length=0;
    }

    this.getSize=function (){
        return ItemCollection.length;
    }


    this.contain=function (key){
        for (var i=0;i<ItemCollection.length ;i++ )
        {
            if (ItemCollection[i].getKey()==key)
            {
                return true;
            }
        }

        return false;
    }
    this.keySet=function ()
    {
        for(var itemLen =0;itemLen<ItemCollection.length;itemLen++){

            keyArray.push(ItemCollection[itemLen].getKey());
        }
        // alert("length"+keyArray.toString())
        return (keyArray);
    }
}