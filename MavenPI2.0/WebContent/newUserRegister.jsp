<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN">
<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>
<%@page import="prg.db.Session"%>
<%@page import="prg.db.*,java.util.*"%>
<%@ page import="utils.db.*"%>
<%@ page import="com.progen.account.db.PbOrganisationDAO"%>
<%

boolean isCompanyValid=false;
String userIdStr = "";
if (session.getAttribute("USERID") != null) {
                userIdStr = (String) session.getAttribute("USERID");
            }
        String themeColor="blue";
        if(session.getAttribute("theme")==null)
                session.setAttribute("theme",themeColor);
            else
                themeColor=String.valueOf(session.getAttribute("theme"));
%>
<html>
    <head>
        <meta http-equiv="content-type" content="text/html; charset=utf-8">
        <title>User Registration</title>
        <link type="text/css" href="<%=request.getContextPath()%>/stylesheets/themes/<%=themeColor%>/metadataButton.css" rel="stylesheet" />
        <link rel="stylesheet" href="<%=request.getContextPath()%>/stylesheets/newUserReg.css" type="text/css" media="screen">
        <link type="text/css" href="<%=request.getContextPath()%>/jQuery/jquery/themes/base/ui.theme.css" rel="stylesheet" />

        <script type="text/javascript" src="<%=request.getContextPath()%>/javascript/lib/jquery/js/jquery-1.4.2.min.js"></script>
        <script type="text/javascript" src="<%=request.getContextPath()%>/jQuery/jquery/ui/ui.core.js"></script>
       <%-- <Script language="javascript"  src="<%=request.getContextPath()%>/javascript/UserRegistration.js"></Script>--%>
        <Script language="javascript"  src="<%=request.getContextPath()%>/javascript/scripts.js"></Script>
        <script language="javascript" src="<%=request.getContextPath()%>/javascript/ajaxscript.js"></script>
<!--        <link type="text/css" href="<%=request.getContextPath()%>/stylesheets/metadataButton.css" rel="stylesheet" />-->

        <script type="text/javascript">
            $(document).ready(function(){
                $test=$(".ui-state-default ");
                $test.hover(
                function(){
                    this.style.background="#308DBB";
                    this.style.color="#000";
                },
                function(){
                    this.style.background="#E6E6E6";
                    this.style.color="#000"
                });
                $("#lastname1").val('');
                $("#password1").val('');
                
                });

            function goBack(){
                 parent.cancelUser();
            }
        </script>
        <style type="text/css">
            .demo{
                left:200px;
                top:50px;
                height:auto;
                width:500px;
            }
            .leftcol {
                clear:left;
                float:left;
                width:100%;
                background-color:#e6e6e6;
            }
            h3 {
                color:#336699;
                font-size:1.1em;
                letter-spacing:0.2pt;
                font-family:verdana;
            }

            .label{
                background-color:#bdbdbd;
            }
        </style>


    </head>
    <body >
        <%
        /*   PbBusinessAreaParams params = new PbBusinessAreaParams();
        PbBusinessAreaManager client = new PbBusinessAreaManager();
        Session prgSession = new Session();
        PbReturnObject pbro = client.getPrgBusinessAreaList(prgSession);
        int rowCount=0;
        rowCount =pbro.getRowCount();
        HashMap PriBusinessAreas = new HashMap();
        HashMap SecBusinessAreas = new HashMap();
        ArrayList primBusAreaIds = new ArrayList();
         */
       /* String errorMsg = (String) request.getAttribute("errorMsg");
        String loginId = null;
        String firstName = null;
        String middleName = null;
        String lastName = null;
        String password = null;
        String type = null;
        String email = null;
        String contactNo = null;
        String address = null;
        String city = null;
        String state = null;
        String country = null;
        String pin = null;
        String active = null;
        String pba = null;
        String sba = null;
        String[] pBusAreas = null;
        String[] sBusAreas = null;

        ////////////////////////////////////////////////////////////////////////////////////////.println.println("Register Login Id is: " + (String) request.getAttribute("loginId"));
        if (loginId != null) {
            loginId = (String) request.getAttribute("loginId");
        } else {
            loginId = "";
        }

        if (firstName != null) {
            firstName = (String) request.getAttribute("firstName");
        } else {
            firstName = "";
        }
        if (lastName != null) {
            lastName = (String) request.getAttribute("LastName");
        } else {
            lastName = "";
        }
        if (middleName != null) {
            middleName = (String) request.getAttribute("middleName");
        } else {
            middleName = "";
        }
        if (password != null) {
            password = (String) request.getAttribute("password");
        } else {
            password = "";
        }
        if (email != null) {
            email = (String) request.getAttribute("email");
        } else {
            email = "";
        }
        if (type != null) {
            type = (String) request.getAttribute("userType");
        }

        if (contactNo != null) {
            contactNo = (String) request.getAttribute("contactNo");
        } else {
            contactNo = "";
        }
        if (address != null) {
            address = (String) request.getAttribute("Address");
        } else {
            address = "";
        }
        if (city != null) {
            city = (String) request.getAttribute("city");
        } else {
            city = "";
        }
        if (state != null) {
            state = (String) request.getAttribute("state");
        } else {
            state = "";
        }
        if (country != null) {
            country = (String) request.getAttribute("country");
        } else {
            country = "";
        }
        if (pin != null) {
            pin = (String) request.getAttribute("pin");
        } else {
            pin = "";
        }
        if (active != null) {
            active = (String) request.getAttribute("active");
        } else {
            active = "";
        }
        pba = (String) request.getAttribute("primBusAreas");
        ////////////////////////////////////////////////////////////////////////////////////////.println.println("pba============" + pba);
        if (pba != null) {
            pBusAreas = pba.split(",");
            ////////////////////////////////////////////////////////////////////////////////////////.println.println("====pBusAreas===" + pBusAreas);
        }
        sba = (String) request.getAttribute("secBusAreas");
        ////////////////////////////////////////////////////////////////////////////////////////.println.println("sba============" + sba);
        if (sba != null) {
            sBusAreas = sba.split(",");
            ////////////////////////////////////////////////////////////////////////////////////////.println.println("====sBusAreas===" + sBusAreas);
        }
        ////////////////////////////////////////////////////////////////////////////////////////.println.println("-----------pba--------" + pba);
        ////////////////////////////////////////////////////////////////////////////////////////.println.println("-----------pba--------" + sba);


        ////////////////////////////////////////////////////////////////////////////////////////.println.println("Login Id is----------------: " + request.getAttribute("loginId"));
 * */
        %>
         <%//session.setAttribute("PriBusinessAreas",PriBusinessAreas);
        String cpath = request.getContextPath();
        PbOrganisationDAO orgDao=new PbOrganisationDAO();
        PbReturnObject orgObj=orgDao.getAccountNames();

        PbDb pbdb=new PbDb();
        String userTypeQ="select * from prg_user_type";
        PbReturnObject userTypeObj=new PbReturnObject();
        userTypeObj=pbdb.execSelectSQL(userTypeQ);
                %>
        <div id="demo" class="demo">
            <form name="myForm" id="myForm" method="post" action="">
<center>
                <div id="leftcol" class='leftcol' width="100%">
                    <center><font size="1px"> Fields marked <span style="color:red">*</span> are MANDATORY </font></center>

                    <table width="100%"  >
                        <tr width="100%">
                            <table width="100%"><tr>
                                    <td width="50%"><label class="label"><span style="color:red">*</span>User Id</label></td>
                                    <td width="50%"><input type="text" name="loginId" style="width:90%" title="Enter User Id(Min.4 Characters)"></td>
                            </tr></table>
                        </tr>
                        <!-- showlegent-->
                        <tr width="100%">
                            <table width="100%"><tr>
                                    <td width="50%"><label class="label"><span style="color:red">*</span>First Name</label></td>
                                    <td width="50%"><input type="text" name="firstName" style="width:90%" title="Enter First Name"></td>
                            </tr></table>
                        </tr>
                        <!-- legentLocation-->
                        <tr width="100%">
                            <table width="100%"><tr>
                                    <td width="50%"><label class="label">Middle Name</label></td>
                                    <td width="50%"><input type="text" name="middleName" style="width:90%" title="Enter Middle Name" ></td>
                            </tr></table>
                        </tr>
                        <!-- showGridX-->
                        <tr width="100%">
                            <table width="100%"><tr>
                                    <td width="50%"><label class="label"><span style="color:red">*</span>Last Name</label></td>
                                    <td width="50%"><input type="text" name="lastName" style="width:90%" title="Enter Last Name1 " id="lastname1" ></td>
                            </tr></table>
                        </tr>

                        <!-- showGridY-->
                        <tr width="100%">
                            <table width="100%"><tr>
                                    <td width="50%"><label class="label"><span style="color:red">*</span>Password</label></td>
                                    <td width="50%"><input type="password" id ="password1" name="password" style="width:90%" title="Enter Password(Min. 4 Characters)" ></td>
                            </tr></table>
                        </tr>
                        <!-- Y-axis Label-->
                        <tr width="100%">
                            <table width="100%"><tr>
                                    <td width="50%"><label class="label"><span style="color:red">*</span>Re-Type Password</label></td>
                                    <td width="50%"><input type="password" name="confirmPassword" style="width:90%" title="Confirm Password" ></td>
                            </tr></table>
                        </tr>
                        <!--  DrillDownt-->
                      <!--  <tr width="100%">
                            <table width="100%"><tr>
                                    <td width="50%"><label  class="label"><span style="color:red">*</span>User Type</label></td>
                                    <td width="25%"><label><input type="radio" name="puType"   value="1">Admin User</label></td>
                                    <td width="25%"><label><input type="radio"  name="puType" checked="checked"  value="2">End User</label></td>
                            </tr></table>
                        </tr>-->
                        <!-- Background Color-->
                        <tr width="100%">
                            <table width="100%"><tr>
                                    <td width="50%"><label class="label">Email Id</label></td>
                                    <td width="50%"><input type="text" style="width:90%" name="email"   title="Enter Email Id"></td>
                            </tr></table>
                        </tr>
<%-- un commment this tr for indicus only--%>
               <%if(isCompanyValid){%>
<!--                       <tr width="100%">
                            <table width="100%"><tr>
                                    <td width="50%"><label class="label"><span style="color:red">*</span>Company Name</label></td>
                                    <td width="50%">
                                        <select name="account" id="account">

                                          <option value="">--Select-</option>

                                            <%  for(int i=0;i<orgObj.getRowCount();i++){%>
                                            <option value="<%=orgObj.getFieldValueString(i,"ORG_ID")%>"><%=orgObj.getFieldValueString(i,"ORGANISATION_NAME")%></option>

                                            <%}%>
                                        </select>
                                    </td>
                            </tr></table>
                        </tr>-->
                        <%}%>
   <Tr>
                            <table width="100%" style="display:none"><tr>
                                    <td width="50%"><label class="label"><span style="color:red">*</span>User Type</label></td>
                                    <td width="50%">
                                        <select name="uType" id="uType">
                                          <option value="10002">Analyzer</option>
<!--                                           <option value="">--Select-</option>
                                          <option value="9999">Admin</option>-->
                                        </select>
                                    </td>
                            </tr></table>

                        </Tr> 
                        <!-- Font Color-->
<!--                        <tr width="100%">
                            <table width="100%"><tr>
                                    <td width="50%"><label class="label">Contact No</label></td>
                                    <td width="50%"><input type="text" style="width:90%" name="contactNo" title="Enter Contact No" onkeypress="return isNumberKey(event)"></td>
                            </tr></table>
                        </tr>-->
                        <!--Show Data Values-->
<!--                        <tr width="100%">
                            <table width="100%"><tr>
                                    <td width="50%"><label class="label">Address</label></td>
                                    <td width="50%"><input type="text" style="width:90%" name="address" title="Enter Address" ></td>
                            </tr></table>
                        </tr>-->
<!--                        <tr width="100%">
                            <table width="100%"><tr>
                                    <td width="50%"><label class="label">City</label></td>
                                    <td width="50%"><input type="text"  name="city" style="width:90%" title="Enter City" ></td>
                            </tr></table>
                        </tr>-->
<!--                        <tr width="100%">
                            <table width="100%"><tr>
                                    <td width="50%"><label class="label">State</label></td>
                                    <td width="50%"><input type="text" name="state" style="width:90%" title="Enter State" ></td>
                            </tr></table>
                        </tr>-->
<!--                        <tr width="100%">
                            <table width="100%"><tr>
                                    <td width="50%"><label class="label">Country</label></td>
                                    <td width="50%">
                                        <select name="puCountry" style="width:92%" title="Enter Country">
                                            <option value="Afghanistan">Afghanistan</option>
                                            <option value="Albania">Albania</option>
                                            <option value="Algeria">Algeria</option>
                                            <option value="American Samoa">American Samoa</option>
                                            <option value="Andorra">Andorra</option>
                                            <option value="Angola">Angola</option>
                                            <option value="Anguilla">Anguilla</option>
                                            <option value="Antarctica">Antarctica</option>
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
                                            <option value="Botswana">Botswana</option>
                                            <option value="Bouvet">Bouvet</option>
                                            <option value="Brazil">Brazil</option>
                                            <option value="Brunei Darussalam">Brunei Darussalam</option>
                                            <option value="Bulgaria">Bulgaria</option>
                                            <option value="Burkina Faso">Burkina Faso</option>
                                            <option value="Burundi">Burundi</option>
                                            <option value="Cambodia">Cambodia</option>
                                            <option value="Cameroon">Cameroon</option>
                                            <option value="Canada">Canada</option>
                                            <option value="Cape Verde">Cape Verde</option>
                                            <option value="Cayman Islands">Cayman</option>
                                            <option value="Chad">Chad</option>
                                            <option value="Chile">Chile</option>
                                            <option value="China">China</option>
                                            <option value="Christmas">Christmas</option>
                                            <option value="Cocos">Cocos</option>
                                            <option value="Colombia">Colombia</option>
                                            <option value="Comoros">Comoros</option>
                                            <option value="Congo">Congo</option>
                                            <option value="Congo">Congo</option>
                                            <option value="Cook Islands">Cook</option>
                                            <option value="Costa Rica">Costa Rica</option>
                                            <option value="Cote D'ivoire">Cote D'ivoire</option>
                                            <option value="Croatia">Croatia</option>
                                            <option value="Cuba">Cuba</option>
                                            <option value="Cyprus">Cyprus</option>
                                            <option value="Czech">Czech</option>
                                            <option value="Denmark">Denmark</option>
                                            <option value="Djibouti">Djibouti</option>
                                            <option value="Dominica">Dominica</option>
                                            <option value="Dominican">Dominican</option>
                                            <option value="Ecuador">Ecuador</option>
                                            <option value="Egypt">Egypt</option>
                                            <option value="El Salvador">El Salvador</option>
                                            <option value="Equatorial Guinea">Equatorial Guinea</option>
                                            <option value="Eritrea">Eritrea</option>
                                            <option value="Estonia">Estonia</option>
                                            <option value="Ethiopia">Ethiopia</option>
                                            <option value="Falkland Islands (Malvinas)">Falkland</option>
                                            <option value="Faroe Islands">Faroe</option>
                                            <option value="Fiji">Fiji</option>
                                            <option value="Finland">Finland</option>
                                            <option value="France">France</option>
                                            <option value="French Guiana">French Guiana</option>
                                            <option value="French Polynesia">French Polynesia</option>
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
                                            <option value="Guinea-bissau">Guinea-bissau</option>
                                            <option value="Guyana">Guyana</option>
                                            <option value="Haiti">Haiti</option>
                                            <option value="Honduras">Honduras</option>
                                            <option value="Hong Kong">Hong Kong</option>
                                            <option value="Hungary">Hungary</option>
                                            <option value="Iceland">Iceland</option>
                                            <option value="India" selected="selected">India</option>
                                            <option value="Indonesia">Indonesia</option>
                                            <option value="Iran, Islamic Republic of">Iran</option>
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
                                            <option value="Korea">Korea</option>
                                            <option value="Kuwait">Kuwait</option>
                                            <option value="Kyrgyzstan">Kyrgyzstan</option>
                                            <option value="Latvia">Latvia</option>
                                            <option value="Lebanon">Lebanon</option>
                                            <option value="Lesotho">Lesotho</option>
                                            <option value="Liberia">Liberia</option>
                                            <option value="Liechtenstein">Liechtenstein</option>
                                            <option value="Lithuania">Lithuania</option>
                                            <option value="Luxembourg">Luxembourg</option>
                                            <option value="Macao">Macao</option>
                                            <option value="Macedonia">Macedonia</option>
                                            <option value="Madagascar">Madagascar</option>
                                            <option value="Malawi">Malawi</option>
                                            <option value="Malaysia">Malaysia</option>
                                            <option value="Maldives">Maldives</option>
                                            <option value="Mali">Mali</option>
                                            <option value="Malta">Malta</option>
                                            <option value="Marshall">Marshall</option>
                                            <option value="Martinique">Martinique</option>
                                            <option value="Mauritania">Mauritania</option>
                                            <option value="Mauritius">Mauritius</option>
                                            <option value="Mayotte">Mayotte</option>
                                            <option value="Mexico">Mexico</option>
                                            <option value="Micronesia">Micronesia</option>
                                            <option value="Moldova">Moldova</option>
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
                                            <option value="New Caledonia">New Caledonia</option>
                                            <option value="New Zealand">New Zealand</option>
                                            <option value="Nicaragua">Nicaragua</option>
                                            <option value="Niger">Niger</option>
                                            <option value="Nigeria">Nigeria</option>
                                            <option value="Niue">Niue</option>
                                            <option value="Norfolk">Norfolk</option>
                                            <option value="Northern Mariana">Northern Mariana</option>
                                            <option value="Norway">Norway</option>
                                            <option value="Oman">Oman</option>
                                            <option value="Pakistan">Pakistan</option>
                                            <option value="Palau">Palau</option>
                                            <option value="Palestina">Palestina</option>
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
                                            <option value="Russia">Russia</option>
                                            <option value="Rwanda">Rwanda</option>
                                            <option value="Saint Helena">Saint Helena</option>
                                            <option value="Saint Lucia">Saint Lucia</option>
                                            <option value="Samoa">Samoa</option>
                                            <option value="San Marino">San Marino</option>
                                            <option value="Saudi Arabia">Saudi Arabia</option>
                                            <option value="Senegal">Senegal</option>
                                            <option value="Seychelles">Seychelles</option>
                                            <option value="Sierra Leone">Sierra Leone</option>
                                            <option value="Singapore">Singapore</option>
                                            <option value="Slovakia">Slovakia</option>
                                            <option value="Slovenia">Slovenia</option>
                                            <option value="Solomon">Solomon</option>
                                            <option value="Somalia">Somalia</option>
                                            <option value="South Africa">South Africa</option>
                                            <option value="Spain">Spain</option>
                                            <option value="Sri Lanka">Sri Lanka</option>
                                            <option value="Sudan">Sudan</option>
                                            <option value="Suriname">Suriname</option>
                                            <option value="Swaziland">Swaziland</option>
                                            <option value="Sweden">Sweden</option>
                                            <option value="Switzerland">Switzerland</option>
                                            <option value="Syrian Arab">Syrian Arab</option>
                                            <option value="Taiwan">Taiwan</option>
                                            <option value="Tajikistan">Tajikistan</option>
                                            <option value="Tanzania">Tanzania</option>
                                            <option value="Thailand">Thailand</option>
                                            <option value="Timor-leste">Timor-leste</option>
                                            <option value="Togo">Togo</option>
                                            <option value="Tokelau">Tokelau</option>
                                            <option value="Tonga">Tonga</option>
                                            <option value="Trinidad and Tobago">Trinidad & Tobago</option>
                                            <option value="Tunisia">Tunisia</option>
                                            <option value="Turkey">Turkey</option>
                                            <option value="Turkmenistan">Turkmenistan</option>
                                            <option value="Turks and Caicos">Turks & Caicos</option>
                                            <option value="Tuvalu">Tuvalu</option>
                                            <option value="Uganda">Uganda</option>
                                            <option value="Ukraine">Ukraine</option>
                                            <option value="United Arab Emirates">U.A.E.</option>
                                            <option value="United Kingdom">United Kingdom</option>
                                            <option value="United States">United States</option>
                                            <option value="Uruguay">Uruguay</option>
                                            <option value="Uzbekistan">Uzbekistan</option>
                                            <option value="Vanuatu">Vanuatu</option>
                                            <option value="Venezuela">Venezuela</option>
                                            <option value="Viet Nam">Viet Nam</option>
                                            <option value="Virgin, British">Virgin(British)</option>
                                            <option value="Virgin, U.S.">Virgin(U.S.)</option>
                                            <option value="Wallis and Futuna">Wallis & Futuna</option>
                                            <option value="Western Sahara">Western Sahara</option>
                                            <option value="Yemen">Yemen</option>
                                            <option value="Zambia">Zambia</option>
                                            <option value="Zimbabwe">Zimbabwe</option>
                                        </select>
                                    </td>
                            </tr></table>
                        </tr>-->
<!--                        <tr width="100%">
                            <table width="100%"><tr>
                                    <td width="50%"><label class="label">PinCode</label></td>
                                    <td width="50%"><input name="pin" type="text" style="width:90%" title="Enter PinCode"  onkeypress="return isNumberKey(event)"></td>
                            </tr></table>
                        </tr>-->
                       <!-- <tr width="100%">
                            <table width="100%" >
                                <tr>
                                    <td width="50%"><label class="label"><span style="color:red">*</span>Active</label></td>
                                    <td width="25%"><label><input type="radio"  name="puActive" checked="checked" onclick="check(this.value)" value="Yes">Yes</label></td>
                                    <td width="25%"><label><input type="radio" name="puActive" onclick="check(this.value)" value="No">No</label></td>
                                </tr>
                            </table>
                        </tr>-->
                    </table>


                     <table align="center"><tr>
                       <%-- <td>
                            <input type="button" class="ui-state-default ui-corner-all" id="Review" value="Review" onclick="return validate();" >
                        </td>--%>
                         <td>
                            <input type="button" class="navtitle-hover" style="width:auto"   id="cancel" value="Cancel" onclick="goBack();" >
                        </td>
                        <td>
                            <input type="button" class="navtitle-hover" style="width:auto" id="Save" value="Save" onclick="return validate();" >
                        </td>

                        <td>
                            <input type="reset" class="navtitle-hover" style="width:auto"  id="reset" value="Reset"  >
                                <input type="hidden" value="<%=cpath%>" id="h">
                                <input type="hidden" value="" id="AssignReports" name="AssignReports">
                                <input type="hidden" value="<%=userIdStr%>" id="loginUserId" name="loginUserId">
                        </td>
                </tr></table>

                </div>
</center>

            </form>

        </div>



    </body>
</html>