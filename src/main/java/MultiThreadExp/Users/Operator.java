package MultiThreadExp.Users;

import MultiThreadExp.Objects.User;
import MultiThreadExp.UserActions;
import MultiThreadExp.Utils;

public class Operator extends User {

    public Operator(String name, String password, String role) {
        super(name, password, role);
    }

    @Override
    public void showMenu() {
        Utils.log("****欢迎进入档案录入员菜单****");
        Utils.log("1. 上传文件");
        Utils.log("2. 下载文件");
        Utils.log("3. 文件列表");
        Utils.log("4. 修改密码");
        Utils.log("5. 退出系统");
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
                    if (UserActions.uploadDoc(this)) {
                        Utils.log("上传成功");
                    } else {
                        Utils.log("上传失败");
                    }
                    break;
                }

                case 2: {
                    if (UserActions.downloadDoc()) {
                        Utils.log("下载成功");
                    } else {
                        Utils.log("下载失败");
                    }
                    break;
                }

                case 3: {
                    UserActions.listFiles();
                    break;
                }

                case 4: {
                    if (UserActions.changePassword(this)) {
                        Utils.log("修改成功");
                    } else {
                        Utils.log("修改失败");
                    }
                    break;
                }

                case 5: {
                    UserActions.exit();
                    return;
                }
            }
        }
    }
}
