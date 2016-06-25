<%@page contentType="text/html" pageEncoding="UTF-8" import="com.progen.metadata.Cube,prg.db.PbReturnObject,java.sql.Connection,java.sql.ResultSet,java.sql.PreparedStatement,prg.db.PbDb,utils.db.ProgenConnection"%>
<%--
    Document   : CustomSetting
    Created on : 26 Aug, 2012, 2:47:11 PM
    Author     : Mohit
--%>

<!DOCTYPE html>
<!--Added by mohit for user Registration,        ******************           -->

         <% String themeColor = "blue";
            if (session.getAttribute("theme") == null) {
                session.setAttribute("theme", themeColor);
            } else {
                themeColor = String.valueOf(session.getAttribute("theme"));
            }
         String userId = String.valueOf(session.getAttribute("USERID"));
          PbDb pbdb = new PbDb();
        // String getfolderList = "SELECT FOLDER_ID, GRP_ID, FOLDER_NAME FROM PRG_USER_FOLDER";
            PbReturnObject pbro = pbdb.execSelectSQL("SELECT USER_TYPE_NAME FROM PRG_USER_TYPE_REGISTRATION");
            String ContextPath=request.getContextPath();
         %>

         <html>
    <head>
       <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <script src="<%=ContextPath%>/javascript/lib/jquery/js/jquery-1.4.2.min.js" type="text/javascript"></script>
        <script type="text/javascript" src="<%=ContextPath%>/javascript/lib/jquery/js/jquery-ui-1.7.3.custom.min.js"></script>
        <link type="text/css" href="<%=ContextPath%>/stylesheets/themes/blue/jquery-ui-1.7.3.custom.css" rel="stylesheet" />
        <script src="<%=ContextPath%>/javascript/treeview/jquery.cookie.js" type="text/javascript"></script>
        <script src="<%=ContextPath%>/javascript/treeview/jquery.treeview.js" type="text/javascript"></script>
        <script type="text/javascript" src="<%=ContextPath%>/javascript/treeview/demo.js"></script>
        <script type="text/javascript" src="<%=ContextPath%>/javascript/quicksearch.js"></script>
        <link type="text/css" href="<%=ContextPath%>/stylesheets/themes/<%=themeColor%>/metadataButton.css" rel="stylesheet" />
        <link rel="stylesheet" href="<%=ContextPath%>/stylesheets/treeviewstyle/jquery.treeview.css" />
        <link rel="stylesheet" href="<%=ContextPath%>/stylesheets/treeviewstyle/screen.css" />
        <link rel="stylesheet" href="stylesheets/treeviewstyle/screen.css" />
        <link type="text/css" href="<%=ContextPath%>/stylesheets/themes/<%=themeColor%>/TableCss.css" rel="stylesheet" />
<!--        <link href="http://code.google.com/apis/maps/documentation/javascript/examples/standard.css" rel="stylesheet" type="text/css" />-->
         <script type="text/javascript" src="<%=ContextPath%>/jQuery/jquery/ui/ui.datepicker.js"></script>
        <script type="text/javascript" src="<%= ContextPath%>//dragAndDropTable.js"></script>

     <style type="text/css">
         .migrate
            {
                font-family: Verdana, Arial, Helvetica, sans-serif;
                font-size: 10pt;
                font-weight: bold;
                color: #3A457C;
                padding-left:12px;
                background-color:#b4d9ee;
                border:0px;
            }
      </style>
</head>
    <body>
<div id="RegistrationForm" title="Add Buyer" align="center" style='position:absolute; left: 450px; top: 50px;'>
    <form id="myregs" method="post" action="<%=request.getContextPath()%>/baseAction.do?param=loginPage" style="border: 2px solid #B4D9EE;">
        <table  style="width:90%;border-collapse: collapse;padding: 0.6em;" id="">
            <tr>
                <td align="center" colspan="2" >
                        <h1>User Registration Form
                        </h1></td>
            </tr>
            
             <tr>
               <td colspan="2" align="center" style="height:1px;padding: 0.6em; border: 1px solid #F0A0A0; " ><font size="5px" > Fields marked <span style="color:#FF0303">*</span> are MANDATORY </font></td>
             </tr><tr><td><br></td></tr>
           <tr><td align="left" style="width:150px; background-color: #B4D9EE; padding: 0.6em; border: 1px solid #CCC; "><label><span style="color:red">*</span>Title : &nbsp;&nbsp;&nbsp;&nbsp;</label></td>
                           <td align="left" style="padding: 0.6em;"><select name="bbn" id="title" style="width:120px;" onchange="">
                                                                 <option selected value="Mr">Mr.</option>
<option value="Mrs">Mrs.</option>
<option value="Miss">Miss</option>
<option value="Ms">Ms</option>
<option value="Dr">Dr.</option>
<option value="Professor">Professor</option>
                             </select></td>
                       </tr>
                       <tr><td align="left" style="width:50px; background-color: #B4D9EE; padding: 0.6em; border: 1px solid #CCC; "><label><span style="color:red">*</span>First Name:&nbsp;&nbsp;</label></td>
                          <td style="padding: 0.6em;"><input id="fn" type="text" style="padding: 0.6em;height:15px;" placeholder="First Name"></td>
                       </tr>
                       <tr><td align="left" style="width:150px; background-color: #B4D9EE; padding: 0.6em; border: 1px solid #CCC; "><label>Last Name:&nbsp;&nbsp;</label></td>
                          <td style="padding: 0.6em;"><input id="ln" type="text" style="padding: 0.6em;height:15px;" placeholder="Last Name"></td>
                       </tr>
                       <tr><td align="left" style="width:150px; background-color: #B4D9EE; padding: 0.6em; border: 1px solid #CCC; "><label><span style="color:red">*</span>Email Id:&nbsp;&nbsp;</label></td>
                          <td style="padding: 0.6em;"><input name="phone" id="email" type="text" style="padding: 0.6em;height:15px;" placeholder="Email Id"></td>
                       </tr>
                       <tr><td align="left" style="width:150px; background-color: #B4D9EE; padding: 0.6em; border: 1px solid #CCC; "><label><span style="color:red">*</span>Mobile No.:&nbsp;&nbsp;</label></td>
                          <td style="padding: 0.6em;"><input id="mob" type="text" style="padding: 0.6em;height:15px;" placeholder="Mobile Number"></td>
                       </tr>
                       <tr><td align="left" style="width:150px; background-color: #B4D9EE; padding: 0.6em; border: 1px solid #CCC; "><label><span style="color:red">*</span>Nationality: &nbsp;&nbsp;&nbsp;&nbsp;</label></td>
                           <td align="left" style="padding: 0.6em;"><select name="bbn" id="nationality" style="width:120px;" onchange="">
                                                                 <option selected value="Indian">Indian</option>
<option value="Other">Other</option>

                             </select></td>
                       </tr>



                       <tr><td align="left" style="width:150px; background-color: #B4D9EE; padding: 0.6em; border: 1px solid #CCC; "><label><span style="color:red">*</span>Country : &nbsp;&nbsp;&nbsp;&nbsp;</label></td>
                       <td align="left" style="padding: 0.6em;">
                       <select id="country" style="width:120px;">
<option value="Afghanistan">Afghanistan</option>
<option value="Albania">Albania</option>
<option value="Algeria">Algeria</option>
<option value="American Samoa">American Samoa</option>
<option value="Andorra">Andorra</option>
<option value="Angola">Angola</option>
<option value="Anguilla">Anguilla</option>
<option value="Antartica">Antarctica</option>
<option value="Antigua and Barbuda">Antigua and Barbuda</option>
<option value="Argentina">Argentina</option>
<option value="Armenia">Armenia</option>
<option value="Aruba">Aruba</option>
<option value="Australia">Australia</option>
<option value="Austria">Austria</option>
<option value="Azerbaijan">Azerbaijan</option>
<option value="Bahamas">Bahamas</option>
<option value="Bahrain">Bahrain</option>
<option value="Bangladesh">Bangladesh</option>
<option value="Barbados">Barbados</option>
<option value="Belarus">Belarus</option>
<option value="Belgium">Belgium</option>
<option value="Belize">Belize</option>
<option value="Benin">Benin</option>
<option value="Bermuda">Bermuda</option>
<option value="Bhutan">Bhutan</option>
<option value="Bolivia">Bolivia</option>
<option value="Bosnia and Herzegowina">Bosnia and Herzegowina</option>
<option value="Botswana">Botswana</option>
<option value="Bouvet Island">Bouvet Island</option>
<option value="Brazil">Brazil</option>
<option value="British Indian Ocean Territory">British Indian Ocean Territory</option>
<option value="Brunei Darussalam">Brunei Darussalam</option>
<option value="Bulgaria">Bulgaria</option>
<option value="Burkina Faso">Burkina Faso</option>
<option value="Burundi">Burundi</option>
<option value="Cambodia">Cambodia</option>
<option value="Cameroon">Cameroon</option>
<option value="Canada">Canada</option>
<option value="Cape Verde">Cape Verde</option>
<option value="Cayman Islands">Cayman Islands</option>
<option value="Central African Republic">Central African Republic</option>
<option value="Chad">Chad</option>
<option value="Chile">Chile</option>
<option value="China">China</option>
<option value="Christmas Island">Christmas Island</option>
<option value="Cocos Islands">Cocos (Keeling) Islands</option>
<option value="Colombia">Colombia</option>
<option value="Comoros">Comoros</option>
<option value="Congo">Congo</option>
<option value="Congo">Congo, the Democratic Republic of the</option>
<option value="Cook Islands">Cook Islands</option>
<option value="Costa Rica">Costa Rica</option>
<option value="Cota D'Ivoire">Cote d'Ivoire</option>
<option value="Croatia">Croatia (Hrvatska)</option>
<option value="Cuba">Cuba</option>
<option value="Cyprus">Cyprus</option>
<option value="Czech Republic">Czech Republic</option>
<option value="Denmark">Denmark</option>
<option value="Djibouti">Djibouti</option>
<option value="Dominica">Dominica</option>
<option value="Dominican Republic">Dominican Republic</option>
<option value="East Timor">East Timor</option>
<option value="Ecuador">Ecuador</option>
<option value="Egypt">Egypt</option>
<option value="El Salvador">El Salvador</option>
<option value="Equatorial Guinea">Equatorial Guinea</option>
<option value="Eritrea">Eritrea</option>
<option value="Estonia">Estonia</option>
<option value="Ethiopia">Ethiopia</option>
<option value="Falkland Islands">Falkland Islands (Malvinas)</option>
<option value="Faroe Islands">Faroe Islands</option>
<option value="Fiji">Fiji</option>
<option value="Finland">Finland</option>
<option value="France">France</option>
<option value="France Metropolitan">France, Metropolitan</option>
<option value="French Guiana">French Guiana</option>
<option value="French Polynesia">French Polynesia</option>
<option value="French Southern Territories">French Southern Territories</option>
<option value="Gabon">Gabon</option>
<option value="Gambia">Gambia</option>
<option value="Georgia">Georgia</option>
<option value="Germany">Germany</option>
<option value="Ghana">Ghana</option>
<option value="Gibraltar">Gibraltar</option>
<option value="Greece">Greece</option>
<option value="Greenland">Greenland</option>
<option value="Grenada">Grenada</option>
<option value="Guadeloupe">Guadeloupe</option>
<option value="Guam">Guam</option>
<option value="Guatemala">Guatemala</option>
<option value="Guinea">Guinea</option>
<option value="Guinea-Bissau">Guinea-Bissau</option>
<option value="Guyana">Guyana</option>
<option value="Haiti">Haiti</option>
<option value="Heard and McDonald Islands">Heard and Mc Donald Islands</option>
<option value="Holy See">Holy See (Vatican City State)</option>
<option value="Honduras">Honduras</option>
<option value="Hong Kong">Hong Kong</option>
<option value="Hungary">Hungary</option>
<option value="Iceland">Iceland</option>
<option selected value="India">India</option>
<option value="Indonesia">Indonesia</option>
<option value="Iran">Iran (Islamic Republic of)</option>
<option value="Iraq">Iraq</option>
<option value="Ireland">Ireland</option>
<option value="Israel">Israel</option>
<option value="Italy">Italy</option>
<option value="Jamaica">Jamaica</option>
<option value="Japan">Japan</option>
<option value="Jordan">Jordan</option>
<option value="Kazakhstan">Kazakhstan</option>
<option value="Kenya">Kenya</option>
<option value="Kiribati">Kiribati</option>
<option value="Democratic People's Republic of Korea">Korea, Democratic People's Republic of</option>
<option value="Korea">Korea, Republic of</option>
<option value="Kuwait">Kuwait</option>
<option value="Kyrgyzstan">Kyrgyzstan</option>
<option value="Lao">Lao People's Democratic Republic</option>
<option value="Latvia">Latvia</option>
<option value="Lebanon">Lebanon</option>
<option value="Lesotho">Lesotho</option>
<option value="Liberia">Liberia</option>
<option value="Libyan Arab Jamahiriya">Libyan Arab Jamahiriya</option>
<option value="Liechtenstein">Liechtenstein</option>
<option value="Lithuania">Lithuania</option>
<option value="Luxembourg">Luxembourg</option>
<option value="Macau">Macau</option>
<option value="Macedonia">Macedonia, The Former Yugoslav Republic of</option>
<option value="Madagascar">Madagascar</option>
<option value="Malawi">Malawi</option>
<option value="Malaysia">Malaysia</option>
<option value="Maldives">Maldives</option>
<option value="Mali">Mali</option>
<option value="Malta">Malta</option>
<option value="Marshall Islands">Marshall Islands</option>
<option value="Martinique">Martinique</option>
<option value="Mauritania">Mauritania</option>
<option value="Mauritius">Mauritius</option>
<option value="Mayotte">Mayotte</option>
<option value="Mexico">Mexico</option>
<option value="Micronesia">Micronesia, Federated States of</option>
<option value="Moldova">Moldova, Republic of</option>
<option value="Monaco">Monaco</option>
<option value="Mongolia">Mongolia</option>
<option value="Montserrat">Montserrat</option>
<option value="Morocco">Morocco</option>
<option value="Mozambique">Mozambique</option>
<option value="Myanmar">Myanmar</option>
<option value="Namibia">Namibia</option>
<option value="Nauru">Nauru</option>
<option value="Nepal">Nepal</option>
<option value="Netherlands">Netherlands</option>
<option value="Netherlands Antilles">Netherlands Antilles</option>
<option value="New Caledonia">New Caledonia</option>
<option value="New Zealand">New Zealand</option>
<option value="Nicaragua">Nicaragua</option>
<option value="Niger">Niger</option>
<option value="Nigeria">Nigeria</option>
<option value="Niue">Niue</option>
<option value="Norfolk Island">Norfolk Island</option>
<option value="Northern Mariana Islands">Northern Mariana Islands</option>
<option value="Norway">Norway</option>
<option value="Oman">Oman</option>
<option value="Pakistan">Pakistan</option>
<option value="Palau">Palau</option>
<option value="Panama">Panama</option>
<option value="Papua New Guinea">Papua New Guinea</option>
<option value="Paraguay">Paraguay</option>
<option value="Peru">Peru</option>
<option value="Philippines">Philippines</option>
<option value="Pitcairn">Pitcairn</option>
<option value="Poland">Poland</option>
<option value="Portugal">Portugal</option>
<option value="Puerto Rico">Puerto Rico</option>
<option value="Qatar">Qatar</option>
<option value="Reunion">Reunion</option>
<option value="Romania">Romania</option>
<option value="Russia">Russian Federation</option>
<option value="Rwanda">Rwanda</option>
<option value="Saint Kitts and Nevis">Saint Kitts and Nevis</option>
<option value="Saint LUCIA">Saint LUCIA</option>
<option value="Saint Vincent">Saint Vincent and the Grenadines</option>
<option value="Samoa">Samoa</option>
<option value="San Marino">San Marino</option>
<option value="Sao Tome and Principe">Sao Tome and Principe</option>
<option value="Saudi Arabia">Saudi Arabia</option>
<option value="Senegal">Senegal</option>
<option value="Seychelles">Seychelles</option>
<option value="Sierra">Sierra Leone</option>
<option value="Singapore">Singapore</option>
<option value="Slovakia">Slovakia (Slovak Republic)</option>
<option value="Slovenia">Slovenia</option>
<option value="Solomon Islands">Solomon Islands</option>
<option value="Somalia">Somalia</option>
<option value="South Africa">South Africa</option>
<option value="South Georgia">South Georgia and the South Sandwich Islands</option>
<option value="Span">Spain</option>
<option value="SriLanka">Sri Lanka</option>
<option value="St. Helena">St. Helena</option>
<option value="St. Pierre and Miguelon">St. Pierre and Miquelon</option>
<option value="Sudan">Sudan</option>
<option value="Suriname">Suriname</option>
<option value="Svalbard">Svalbard and Jan Mayen Islands</option>
<option value="Swaziland">Swaziland</option>
<option value="Sweden">Sweden</option>
<option value="Switzerland">Switzerland</option>
<option value="Syria">Syrian Arab Republic</option>
<option value="Taiwan">Taiwan, Province of China</option>
<option value="Tajikistan">Tajikistan</option>
<option value="Tanzania">Tanzania, United Republic of</option>
<option value="Thailand">Thailand</option>
<option value="Togo">Togo</option>
<option value="Tokelau">Tokelau</option>
<option value="Tonga">Tonga</option>
<option value="Trinidad and Tobago">Trinidad and Tobago</option>
<option value="Tunisia">Tunisia</option>
<option value="Turkey">Turkey</option>
<option value="Turkmenistan">Turkmenistan</option>
<option value="Turks and Caicos">Turks and Caicos Islands</option>
<option value="Tuvalu">Tuvalu</option>
<option value="Uganda">Uganda</option>
<option value="Ukraine">Ukraine</option>
<option value="United Arab Emirates">United Arab Emirates</option>
<option value="United Kingdom">United Kingdom</option>
<option value="United States">United States</option>
<option value="United States Minor Outlying Islands">United States Minor Outlying Islands</option>
<option value="Uruguay">Uruguay</option>
<option value="Uzbekistan">Uzbekistan</option>
<option value="Vanuatu">Vanuatu</option>
<option value="Venezuela">Venezuela</option>
<option value="Vietnam">VietNam</option>
<option value="Virgin Islands (British)">Virgin Islands (British)</option>
<option value="Virgin Islands (U.S)">Virgin Islands (U.S.)</option>
<option value="Wallis and Futana Islands">Wallis and Futuna Islands</option>
<option value="Western Sahara">Western Sahara</option>
<option value="Yemen">Yemen</option>
<option value="Yugoslavia">Yugoslavia</option>
<option value="Zambia">Zambia</option>
<option value="Zimbabwe">Zimbabwe</option>
</select></td>
                       </tr>
                       <tr><td align="left" style="width:150px; background-color: #B4D9EE; padding: 0.6em; border: 1px solid #CCC; "><label><span style="color:red">*</span>User Type: &nbsp;&nbsp;&nbsp;&nbsp;</label></td>
                           <td align="left" style="padding: 0.6em;"><select name="ut" id="ut" style="width:120px;" onchange="showstate(this.id)">
                 <option selected value="--SELECT--">--SELECT--</option>
                                   <% 
                                   if (pbro.getRowCount() >0) {
                for (int i = 0; i < pbro.getRowCount(); i++) { %>
                      <option value="<%=pbro.getFieldValueString(i, 0)%>"><%=pbro.getFieldValueString(i, 0)%></option>

                      <%}}%>
                             </select></td>
                       </tr>
                        <tr id="statelist" style="display:none"><td align="left" style="width:150px; background-color: #B4D9EE; padding: 0.6em; border: 1px solid #CCC; "><label><span style="color:red">*</span>State/U.T. : &nbsp;&nbsp;&nbsp;&nbsp;</label></td>
                           <td align="left" style="padding: 0.6em;"><select name="state" id="state" style="width:120px;" onchange="">
                 <option selected value="--SELECT--">--SELECT--</option>
<option value="Andaman and Nicobar Islands">Andaman and Nicobar Islands</option>
<option value="Andhra Pradesh">Andhra Pradesh</option>
<option value="Arunachal Pradesh">Arunachal Pradesh</option>
<option value="Assam">Assam</option>
<option value="Bihar">Bihar</option>
<option value="Chandigarh">Chandigarh</option>
<option value="Chhattisgarh">Chhattisgarh</option>
<option value="Dadra and Nagar Haveli">Dadra and Nagar Haveli</option>
<option value="Daman and Diu">Daman and Diu</option>
<option value="Delhi">Delhi</option>
<option value="Goa">Goa</option>
<option value="Gujarat">Gujarat</option>
<option value="Haryana">Haryana</option>
<option value="Himachal Pradesh">Himachal Pradesh</option>
<option value="Jammu and Kashmir">Jammu and Kashmir</option>
<option value="Jharkhand">Jharkhand</option>
<option value="Karnataka">Karnataka</option>
<option value="Kerala">Kerala</option>
<option value="Lakshadweep">Lakshadweep</option>
<option value="Madhya Pradesh">Madhya Pradesh</option>
<option value="Maharashtra">Maharashtra</option>
<option value="Manipur">Manipur</option>
<option value="Meghalaya">Meghalaya</option>
<option value="Mizoram">Mizoram</option>
<option value="Nagaland">Nagaland</option>
<option value="Orissa">Orissa</option>
<option value="Pondicherry">Pondicherry</option>
<option value="Punjab">Punjab</option>
<option value="Rajasthan">Rajasthan</option>
<option value="Sikkim">Sikkim</option>
<option value="Tamil Nadu">Tamil Nadu</option>
<option value="Tripura">Tripura</option>
<option value="Uttaranchal">Uttaranchal</option>
<option value="Uttar Pradesh">Uttar Pradesh</option>
<option value="West Bengal">West Bengal</option>
                             </select></td>
                       </tr>


                      <tr><td align="left" style="width:150px; background-color: #B4D9EE; padding: 0.6em; border: 1px solid #CCC; "><label>Address:&nbsp;&nbsp;</label></td>
                          <td style="padding: 0.6em;"><textarea id="add" rows="1" cols="30" name="comment" form="usrform"  style="padding: 0.6em;" placeholder="Address" >
</textarea></td>
                       </tr>
                        <tr><td align="left" style="width:150px; background-color: #B4D9EE; padding: 0.6em; border: 1px solid #CCC; "><label><span style="color:red">*</span>Profession:&nbsp;&nbsp;</label></td>
                          <td style="padding: 0.6em;"><input id="prof" type="text" style="height:15px;padding: 0.6em; " placeholder="Profession"></td>
                       </tr>
                       <tr><td align="left" style="width:150px; background-color: #B4D9EE; padding: 0.6em; border: 1px solid #CCC; "><label><span style="color:red">*</span>Purpose:&nbsp;&nbsp;</label></td>
                          <td style="padding: 0.6em;"><textarea  id="purpose" rows="1"  style="padding: 0.6em;" cols="30" name="comment" form="usrform" placeholder="Purpose">
</textarea></td>
                       </tr>
                       <br>
                       <tr></tr>
                       <tr></tr>

                        <tr><td colspan="2"><center><input  class="navtitle-hover" type="button" style="width:auto;color:black" value="Submit" id="" onclick="regUser()"/>
                                  &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input type="submit" class="navtitle-hover" style="width:auto;color:black" value="Cancel" id="" /></center>
                       </td></tr>





        </table>

</form></div>

        <div style="display:none;">
        <form id="" style="width:70%;border: 2px solid #B4D9EE;">
        <table  style="width:100%;border-collapse: collapse;padding: 0.6em;"  align="center" id="">
            <tr >
                <td align="center" colspan="2" >
                        <h1>User Registration Details
                        </h1></td>
            </tr>
           <tr><td align="left" style="width:150px; background-color: #B4D9EE; padding: 0.6em; border: 1px solid #CCC; "><label>Full Name : &nbsp;&nbsp;&nbsp;&nbsp;</label></td>
                           <td align="left" style="padding: 0.6em;"></td></tr>
                       <tr><td align="left" style="width:150px; background-color: #B4D9EE; padding: 0.6em; border: 1px solid #CCC; "><label>Email Id:&nbsp;&nbsp;</label></td>
                          <td style="padding: 0.6em;"></td>
                       </tr>
                       <tr><td align="left" style="width:150px; background-color: #B4D9EE; padding: 0.6em; border: 1px solid #CCC; "><label>Mobile No.:&nbsp;&nbsp;</label></td>
                          <td style="padding: 0.6em;"></td>
                       </tr>
                       <tr><td align="left" style="width:150px; background-color: #B4D9EE; padding: 0.6em; border: 1px solid #CCC; "><label>Nationality: &nbsp;&nbsp;&nbsp;&nbsp;</label></td>
                           <td align="left" style="padding: 0.6em;"></td>
                       </tr>
                       <tr><td align="left" style="width:150px; background-color: #B4D9EE; padding: 0.6em; border: 1px solid #CCC; "><label>Country : &nbsp;&nbsp;&nbsp;&nbsp;</label></td>
                       <td align="left" style="padding: 0.6em;"></td></tr>
                        <tr><td align="left" style="width:150px; background-color: #B4D9EE; padding: 0.6em; border: 1px solid #CCC; "><label>Address:&nbsp;&nbsp;</label></td>
                          <td style="padding: 0.6em;"</td>
                       </tr>
                        <tr><td align="left" style="width:150px; background-color: #B4D9EE; padding: 0.6em; border: 1px solid #CCC; "><label>Profession:&nbsp;&nbsp;</label></td>
                          <td style="padding: 0.6em;"></td>
                       </tr>
                       <tr><td align="left" style="width:150px; background-color: #B4D9EE; padding: 0.6em; border: 1px solid #CCC; "><label>Purpose:&nbsp;&nbsp;</label></td>
                          <td style="padding: 0.6em;"></td>
                       </tr>
                        <br>
                       <tr></tr>
                       <tr></tr>
        </table>
</form></div>
        <div id='loadingmetadata' class='loading_image' style="display:none;">
                <img alt=""  id='imgId' src='images/help-loading.gif'  border='0px' style='position:absolute; left: 450px; top: 150px;'/>
</div>
<script type="text/javascript">
            $(document).ready(function(){







               if ($.browser.msie == true){
//              

               }
               else {
//                 
               }
           });

//           function validEmail(thefield,therlength,themessage,id1)
//{
//	var theinput=thefield.value;
//	var thelength=theinput.length;
//	var goodzip=true;
//	if(thelength<therlength)
//	{
//		goodzip=false;
//	}
//	if(theinput.indexOf("@")==-1)
//	{
//				goodzip=false;
//	}
//	if(goodzip==false)
//	{
//		document.getElementById(id1).innerHTML=themessage
//	}
//	return goodzip;
//}
//
//
//function validString(thefield,therlength,themessage,id1)
//{
//	var theinput=thefield.value;
//	var thelength=theinput.length;
//	var goodzip=true;
//	if(thelength<therlength)
//	{
//		goodzip=false;
//
//	}
//	if(thelength>=therlength)
//	{
//		for(var i=0;i<thelength;i++)
//		{
//			var thechar=theinput.substring(i,i+1);
//		if(!(thechar>="a" && thechar<="z" || thechar>="A" && thechar<="Z" || thechar==" "))
//			{
//				goodzip=false;
//			}
//		}
//	}
//	if(goodzip==false)
//	{
//		document.getElementById(id1).innerHTML=themessage
//
//	}
//	return goodzip;
//}
//
//
//function validNum(thefield,therlength,themessage,id1)
//{
//	var theinput=thefield.value;
//	var thelength=theinput.length;
//	var goodzip=true;
//	if(thelength<therlength)
//	{
//		goodzip=false;
//	}
//	if(thelength>=therlength)
//	{
//		for(var i=0;i<thelength;i++)
//		{
//			var thechar=theinput.substring(i,i+1);
//			if(!((thechar>="0" && thechar<="9")  || thechar=="."))
//			{
//				goodzip=false;
//			}
//		}
//	}
//	if(goodzip==false)
//	{
//		document.getElementById(id1).innerHTML=themessage
//	}
//	return goodzip;
//}
function changeFocus(id)
{
//    alert(id)
     $("#"+id).focus();
       $("#"+id).css('background-color','#FFB2B2')
}



function regUser()
{
var title= document.getElementById("title").value;
//    alert(bName)
//alert("hii")
           var fn = document.getElementById("fn").value;
             var ln = document.getElementById("ln").value;
              var email = document.getElementById("email").value;
               var mob = document.getElementById("mob").value;
               var nationality = document.getElementById("nationality").value;
               var country = document.getElementById("country").value;
               var add = document.getElementById("add").value;
               var prof = document.getElementById("prof").value;
               var purpose = document.getElementById("purpose").value;
                var ut = document.getElementById("ut").value;
                 var state = document.getElementById("state").value;
//               alert(ut)
//                   alert(ln)
//                   alert(email)
//                   alert(mob)
//                       alert(nationality)
//                           alert(country)
//                               alert(add)
//                               alert(prof)
//                                   alert(purpose)

               // alert($("#repTitle").val());
               //
//alert("hello")
// if (!validNum(document.myregs.phone, 10, "Please Enter Valid Phone number", 1500))
//                    return false;

               // alert($("#repTitle").val());
//alert("hello")
                         if(title=='')
                {
                    alert('Please Select Title');
                    changeFocus("title");
                    //window.location.href='#CustomSettings.jsp';
//                  return false;

                }else if(fn==''){
                    alert('Please Enter First Name');
                    changeFocus("fn");
                     //window.location.href='#CustomSettings.jsp';
//                    return false;

                }
//                else if(ln==''){
//                   alert('Please Enter Last Name');
//                   changeFocus("ln");
//                    // window.location.href='#CustomSettings.jsp';
////                    return false;
//
//                }
                else if(email==''){
                    alert('Please Enter Email Id.');
                    changeFocus("email");
                     //window.location.href='#CustomSettings.jsp';
//                  return false;

                }
                else if(mob==''|| isNaN(mob)){
                    alert('Please Enter Valid Mobile No.');
                    changeFocus("mob");
                     //window.location.href='#CustomSettings.jsp';
//                  return false;

                }
                else if(nationality==''){
                    alert('Please Select Nationality');
                    changeFocus("nationality");
                     //window.location.href='#CustomSettings.jsp';
//                  return false;

                }else if(country==''){
                    alert('Please Select Country');
                    changeFocus("country");
                     //window.location.href='#CustomSettings.jsp';
//                    return false;

                }
//                else if(add==''){
//                   alert('Please Enter Address');
//                   changeFocus("add");
//                }
                else if(prof==''){
                   alert('Please Enter Your Profession ');
                   changeFocus("prof");
                }else if(purpose==''){
                   alert('Please Enter Your Purpose');
                   changeFocus("purpose");
                } else if(ut=='--SELECT--'){
                   alert('Please Select User Type');
                   changeFocus("ut");
                }else if(ut=='State User' && state=='--SELECT--'){
                   alert('Please Select State');
                   changeFocus("state");
                }
                else{
//                    alert("hii")
                    $("#RegistrationForm").hide();
                    $("#loadingmetadata").show();
                    $.ajax({
                    async:false,
                    url:'userLayerAction.do?userParam=userRegistration&title='+title+'&fn='+fn+'&ln='+ln+'&email='+email+'&mob='+mob+'&nationality='+nationality+'&country='+country+'&add='+add+'&prof='+prof+'&purpose='+purpose+'&ut='+ut+'&state='+state,
                    success:function(data) {
                         $("#loadingmetadata").hide();
                        if(data.contains("true")){ 
                        alert("Registered Successfully.\n\
                              You Will Receive an Email Shortly"
                              );
                   document.getElementById("myregs").submit();
                      }
                      else if(data.contains("alreadyexists"))
                          {
                             alert("Email Id '"+email+"' is Already Registered With Us..\n\n\
                                    Please Enter a Valid Email Id")
                        $("#RegistrationForm").show();
                        changeFocus("email");
                          }
                    else
                        {
                            alert('Record Not Saved')
                            $("#RegistrationForm").show();
                        }

                
            
             
             


                }
                });
            }}
   

function showstate(id){
// alert("jj")
 if($("#"+id).val()=="State User")
     {
    document.getElementById("statelist").style.display = "";
     }
     else
          document.getElementById("statelist").style.display = "none";
}


       </script>
    </body>

</html>
<!--Added by Mohit jain-->

