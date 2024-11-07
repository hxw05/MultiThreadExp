package MultiThreadExp.Users;

import MultiThreadExp.User;
import MultiThreadExp.UserActions;
import MultiThreadExp.Utils;

public class Browser extends User {
    public Browser(String name, String password, String role) {
        super(name, password, role);
    }

    @Override
    public void showMenu() {
        Utils.log("****欢迎进入档案浏览员菜单****");
        Utils.log("1. 下载文件");
        Utils.log("2. 文件列表");
        Utils.log("3. 修改密码");
        Utils.log("4. 退出系统");
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
                    if (UserActions.downloadFile()) {
                        Utils.log("下载成功");
                    } else {
                        Utils.log("下载失败");
                    }
                    break;
                }

                case 2: {
                    UserActions.listFiles();
                    break;
                }

                case 3: {
                    if (UserActions.changePassword(this)) {
                        Utils.log("修改成功");
                    } else {
                        Utils.log("修改失败");
                    }
                    break;
                }

                case 4: {
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
