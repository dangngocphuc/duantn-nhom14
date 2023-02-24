/* Các xử lý kịch bản với user.hmtl */

// Kiểm tra name
        function checkName(fn) {
            var name = fn.txtUserName.value;
            //var name = document.getElementById("inputName").value;

            //Ghi nhận thông báo
            var message = "";

            //Check name
            name = name.trim();

            document.getElementById("inputEmail").disabled = false;

            if (name == "") {
                message = "Thiếu tên đăng nhập!";
            } else {
                if (name.length < 5 || name.length > 50) {
                    message = "Tên đăng nhập cho phép 6 -50 kí tự!";
                } else if (name.indexOf(" ") != -1) {
                    message = "Tên đăng nhập không được có dấu cách!";
                } else if (name.indexOf("@") != -1) {
                    var parttern = /\w+@\w+[.]\w/;
                    if (!name.match(parttern)) {
                        message = "Không đúng cấu trúc hộp thư.";
                    } else {
                        document.getElementById("inputEmail").disabled = true;
                    }
                }
            }

            //Tham chiếu đối tượng hiển thị lỗi 
            var view = document.getElementById("viewValidName");

            if (message != "") {
                view.innerHTML = message;
                view.style.color = "red";
                return false;
            } else {
                view.innerHTML = "";
                return true;
            }
        }
function checkEmail(fn) {
    var email = fn.txtUserEmail.value;
    //var name = document.getElementById("inputName").value;

    //Ghi nhận thông báo
    var message = "";

    //Check name
    email = email.trim();
    if (email == "") {
        message = "Thiếu hộp thư!";
    } else {
        if (email.indexOf(" ") != -1) {
            message = "Hộp thư không được có dấu cách!";
        } else if (email.indexOf("@") != -1) {
            var parttern = /\w+@\w+[.]\w/;
            if (!email.match(parttern)) {
                message = "Không đúng cấu trúc hộp thư.";
            }else{
                message = "<i class=\"fas fa-check\"></i>";
            }
            
        }
    }
    //Tham chiếu đối tượng hiển thị lỗi 
    var view = document.getElementById("viewValidEmail");

    if (message != "") {
        view.innerHTML = message;
        view.style.color = "red";
        return false;
    } else {
        view.innerHTML = "";
        return true;
    }
}
function checkPass(fn) {
    var pass1 = document.getElementById("inputPass1").value;
    var pass2 = document.getElementById("inputPass2").value;

    var message = "";
    pass1 = pass1.trim();
    var isOk = false;
    if (pass1 == "") {
        message = "Thiếu mật khẩu";
    } else {
        if (pass1.length > 2) {
            if (pass1 != pass2) {
                message = "Mật khẩu nhập lại không khớp";
            } else {
                message = "<i class=\"fas fa-check\"></i>";
                isOk = true;
            }
        } else {
            message = "Mật khẩu cần lớn hơn 5 ký tự";
        }
    }
    // Tham chiếu đối tượng hiển thị lỗi
    var view = document.getElementById("viewValidPass");
    view.innerHTML = message;
    if (isOk) {
        view.style.color = "blue";
        return true;
    } else {
        view.style.color = "red";
        return false;
    }
}
function generatePermis(fn) {
    //Khai báo danh sách quyền thực thi
    var permis = new Array();
    permis[0] = "---------Select---------";
    permis[1] = "Members (Thành viên)";
    permis[2] = "Authors (Tác giả)";
    permis[3] = "Managers (Quản lý)";
    permis[4] = "Administrators (Quản trị)";
    permis[5] = "Super Admin (Quản trị cấp cao)";

    var opt = "";

    opt += "<select class=\"form-control\" id=\"inputPermis\" name=\"slcPermis\" onchange=\"refreshPermis(this.form)\" >";
    for (var i = 0; i < permis.length; i++) {

        opt += "<option value=\"" + i + "\">" + permis[i] + "</option>";
    }
    opt += "</select>";

    //Xuất cấu trúc này ra
    document.write(opt);
}

function generateRoles() {
    //Danh sách vai trò
    var roles = new Array();
    roles[0] = "<i class=\"fas fa-user\"></i> User";
    roles[1] = "<i class=\"fas fa-user\"></i> Sections";
    roles[2] = "<i class=\"fas fa-user\"></i> Category";
    roles[3] = "<i class=\"fas fa-user\"></i> Articles";
    roles[4] = "<i class=\"fas fa-user\"></i> Product System";
    roles[5] = "<i class=\"fas fa-user\"></i> Product Group";
    roles[6] = "<i class=\"fas fa-user\"></i> Product Category";
    roles[7] = "<i class=\"fas fa-user\"></i> Product";
    roles[8] = "<i class=\"fas fa-user\"></i> Order";
    roles[9] = "<i class=\"fas fa-user\"></i> Customer";

    var role = "";
    for (var i = 0; i < roles.length; i++) {
        if (i % 3 == 0) {
            role += "<div class=\"row row1\">";
        }

        role += "<div class=\"col-md-4\">";
        role += "<input class=\"form-check-input\" type=\"checkbox\" id=\"chk" + i + "\" disabled name=\"chks\" onclick=\"checkPermis(this.form)\">";
        role += "<label class=\"form-check-label\" for=\"chk" + i + "\">";
        role += roles[i] + " management";
        role += "</label>";
        role += "</div>";

        if (i % 3 == 2 || i == roles.length - 1) {
            role += "</div>";
        }
    }

    //Xuất cấu trúc này ra
    document.write(role);
}

//Thiết lập các giá trị cho checkbox
function setCheckBox(fn, dis, check) {
    //Duyệt các phần tử của form
    for (var i = 0; i < fn.elements.length; i++) {
        if (fn.elements[i].type == "checkbox" && fn.elements[i].name == "chks") {
            fn.elements[i].disabled = dis;
            fn.elements[i].checked = check;
        }
    }
}

//Thay đổi quyền thực thi
function refreshPermis(fn) {
    //Lấy giá trị quyền thực thi
    var permis = parseInt(document.getElementById("inputPermis").value);

    if (permis == 4 || permis == 5) {
        this.setCheckBox(fn, true, true);
    } else if (permis == 3) {
        this.setCheckBox(fn, false, true);
    } else if (permis == 2) {
        this.setCheckBox(fn, false, false);
    } else {
        this.setCheckBox(fn, true, false);
    }
}

// Kiểm tra quyền
function checkPermis(fn) {

    // Kiểm tra quyền thực thi
    var permis = parseInt(document.getElementById("inputPermis").value);
    var validPermis = false;
    if (permis == 2 || permis == 3) {
        for (var i = 0; i < fn.elements.length; i++) {
            if (fn.elements[i].type == "checkbox" && fn.elements[i].name == "chks") {
                if (fn.elements[i].checked) {
                    validPermis = true;
                    break;
                } else {
                    validPermis = false;
                }
            }
        }
        var message = "";
        var view = document.getElementById("viewValidPermis");
        if (!validPermis) {
            message = "Cần phải có ít nhất 1 vai trò cho quyền này";
            view.innerHTML = message;
            view.style.color = "red";
        } else {
            view.innerHTML = "";
        }

    }
    return validPermis;

}
// Kiểm tra toàn thể
function checkValidUser(fn) {
    var validName = checkName(fn);
    var validPass = checkPass(fn);
    var validPermis = checkPermis(fn);
    if (!validName) {
        document.getElementById("inputName").focus();
        document.getElementById("inputName").select();
    } else if (!validPass) {
        document.getElementById("inputPass1").focus();
        document.getElementById("inputPass1").select();
    } else if (!validPermis) {
        document.getElementById("inputPermis").focus();
        document.getElementById("inputPermis").select();
    }
    return validName && validPass && validPermis;
}