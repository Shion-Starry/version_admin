package app.desty.chat_admin.common.constants;

public interface RouteConstants {

    interface Main {
        String moduleName = "/main";
        String splash = moduleName + "/splash";
        String welcome = moduleName + "/welcome";
    }

    interface Internal {
        String moduleName = "/internal";
        String internal = moduleName + "/internal";
        String getCaptcha = moduleName + "/captcha";
        String socketTest = moduleName + "/socketTest";
    }

    interface Login {
        String moduleName = "/login";
        String login = moduleName + "/login";
        String loginWithOtp = moduleName + "/loginWithOtp";
        String loginWithEmail = moduleName + "/loginWithEmail";
        String loginRefresh = moduleName +"/loginRefresh";
        String completeUserInfo = moduleName + "/completeUserInfo";
    }

    interface Home {
        String moduleName = "/home";
        String homaPage = moduleName + "/home";

    }

    interface Message {
        String moduleName = "/message";
        String home = moduleName + "/message";
        String sessionList = moduleName + "/sessionList";
        String sessionDetail = moduleName + "/sessionDetail";
        String sessionSearch = moduleName + "/sessionSearch";
    }

    interface Product {
        String moduleName = "/product";
        String productList = moduleName + "/productList";
        String productDetail = moduleName + "/productDetail";
    }

    interface Order {
        String moduleName = "/order";
        String orderList = moduleName + "/orderList";
        String orderDetail = moduleName + "/orderDetail";
    }

    interface Setting {
        String moduleName = "/setting";
        String bindStore = moduleName + "/bindStore";
        String bind3rdPart = moduleName +"/bind3rdPart";
        String channelManagement = moduleName + "/channelManagement";
    }
}
