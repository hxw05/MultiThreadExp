package MultiThreadExp.Users;

import MultiThreadExp.DataProcessing;
import MultiThreadExp.User;
import MultiThreadExp.UserActions;
import MultiThreadExp.Utils;

public class Administrator extends User {
    public Administrator(String name, String password, String role) {
        super(name, password, role);
    }

    @Override
    public void showMenu() {
        Utils.log("****欢迎进入管理员菜单****");
        Utils.log("1. 修改用户");
        Utils.log("2. 删除用户");
        Utils.log("3. 新增用户");
        Utils.log("4. 列出用户");
        Utils.log("5. 下载文件");
        Utils.log("6. 文件列表");
        Utils.log("7. 修改本人密码");
        Utils.log("8. 退出系统");
        Utils.log("**************************");
    }

    @Override
    public void startLoop() {
        while (true) {
            showMenu();

            var choice = Utils.read("请选择菜单：", Integer.class);

            if (choice == null) {
                Utils.log("请输入有效的数字");
                continue;
            }

            switch (choice) {
                case 1: {
                    if (UserActions.updateUser()) {
                        Utils.log("修改成功");
                    } else {
                        Utils.log("修改失败");
                    }
                    break;
                }

                case 2: {
                    if (UserActions.deleteUser()) {
                        Utils.log("删除成功");
                    } else {
                        Utils.log("删除失败");
                    }
                    break;
                }

                case 3: {
                    if (UserActions.insertUser()) {
                        Utils.log("新增成功");
                    } else {
                        Utils.log("新增失败");
                    }
                    break;
                }

                case 4: {
                    Utils.log("列出用户");
                    var userEnum = DataProcessing.getAllUser();
                    Utils.log("用户名\t密码\t角色");
                    Utils.listEnum(userEnum, u -> Utils.log(u.getName() + "\t" + u.getPassword() + "\t" + u.getRole()));
                    break;
                }

                case 5: {
                    if (UserActions.downloadFile()) {
                        Utils.log("下载成功");
                    } else {
                        Utils.log("下载失败");
                    }
                    break;
                }

                case 6: {
                    UserActions.listFiles();
                    break;
                }

                case 7: {
                    if (UserActions.changePassword(this)) {
                        Utils.log("修改成功");
                    } else {
                        Utils.log("修改失败");
                    }
                    break;
                }

                case 8: {
                    UserActions.exit();
                    return;
                }

                default: {
                    Utils.log("请输入正确编号");
                }
            }
        }
    }
}
