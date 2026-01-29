#  Simple Shopping Mall (電商購物系統)

這是一個基於 **Spring Boot** 與 **Thymeleaf** 開發的電商購物系統。
模擬了完整的使用者購物流程，包含商品瀏覽、購物車管理、會員登入驗證以及訂單建立。

##  Tech Stack (技術堆疊)

* **Backend**: Java 17, Spring Boot 3
* **Frontend**: Thymeleaf, HTML5
* **Database**: MySQL, Spring Data JPA
* **Security**: Spring Security 
* **Build Tool**: Maven

##  Features (核心功能)

### 1. 權限控管系統 
* **角色權限管理 (Role-Based Access Control)**：
  實作 **Admin** 與 **User** 的權限隔離。
  * `Admin`：擁有後台管理權限，可進行商品的 CRUD 操作。
  * `User`：僅能瀏覽前台商品、操作購物車與結帳。

  ![登入畫面](screenshots/login.jpg)

### 2. 商品管理 (Product Management)
* **前台瀏覽**：使用者可以瀏覽商品並加入購物車。
  ![前台商品列表](screenshots/productlist.jpg)

* **後台管理**：管理員擁有「修改」與「刪除」的權限。
  ![後台管理介面](screenshots/admin.jpg)

### 3. 購物車 (Shopping Cart)
* 使用 **HttpSession** 實作購物車功能，即使刷新頁面也能保留商品。
* 支援動態修改數量、移除商品與總金額自動計算。
  ![購物車畫面](screenshots/car.jpg)
### 4. 訂單處理 (Order Processing)
* 結帳流程整合：將購物車內容轉換為正式訂單 (`Order`) 與訂單明細 (`OrderItem`)。
* 庫存與邏輯檢查：防止無效訂單生成。
![訂單列表](screenshots/order.jpg)

##  How to Run (如何執行)

1.  Clone 此專案到本地：
    ```bash
    git clone [https://github.com/hanklin0822/SHOP-DEMO.git](https://github.com/hanklin0822/SHOP-DEMO.git)
    ```
2.  設定資料庫 (`application.properties`)：
    請確認 MySQL 已啟動，並建立 `shopping_db` 資料庫。
3.  執行專案：
    ```bash
    mvn spring-boot:run
    ```
4.  開啟瀏覽器：
    訪問 `http://localhost:8080`

##  Future Roadmap (未來規劃)

* [ ] 導入 **Docker** 容器化部署
* [ ] 實作 JWT (JSON Web Token) 驗證
* [ ] 串接第三方金流 API

---
*Created by Hank Lin*
