<h1>🪻Perfume Online Shop🪻</h1>
<p>E-commerce project built on Spring Boot.<br/>
  See demo below.</p>
  
<p>User credentials for login:<br/>
Username: user<br/>
Password: user</p>

<p>Admin credentials for login:<br/>
Username: admin<br/>
Password: admin</p>

<p>SuperAdmin credentials for login:<br/>
Username: super_admin<br/>
Password: super_admin</p>

<h2>Used Technologies</h2>
<ul>
  <li>Spring (Boot, Data, Security)</li>
  <li>JPA / JDBC</li>
  <li>PostgreSQL</li>
  <li>Gradle</li>
  <li>Thymeleaf</li>
  <li>Bootstrap, HTML, CSS</li>
  <li>Stripe API</li>
  <li>JUnit, AssertJ, Mockito</li>
</ul>

<h2>About This Project</h2>
<p>Customers can:
  <ul>
    <li>register or log in</li>
    <li>choose a product from the showcase and view it</li>
    <li>filter perfumes by brand, gender, price</li>
    <li>search for a product according to the specified criteria</li>
    <li>add products to the shopping cart and remove products from the cart</li>
    <li>order products from the shopping cart (when purchasing a product, funds are withdrawn from the electronic wallet)</li>
    <li>change their password and view their orders on the account</li>
  </ul>
</p>

<p>Administrators can:
  <ul>
    <li>add or delete a products</li>
    <li>delete users</li>
    <li>reset the user's password by sending an email to the user</li>
  </ul>
</p>

<p>Superadmin can:
  <ul>
    <li>promote regular users to admins</li>
    <li>demote admins to regular users</li>
  </ul>
</p>

<p>Admin panel features include user management, product management and admin managment for superadmin.<br/>
Restriction of access to the admin panel depends on the roles. (Hierarchy of roles: User &lt; Administrator &lt; Superadmin)</p>
