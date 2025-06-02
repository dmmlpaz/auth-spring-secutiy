# Proyecto de Spring security para autenticacion

### Logica de ejecucion

1. Se crea la clase de configuracion WebSecurityConfig
el metodo SecurityFilterChain de esta clase es solo configuracion de spring security
y beans para hacer funcionar el proceso implicitamente , se crean los beans:

    - PasswordEncoder: codificacion de la clave y validacion con Bycript
    - AuthenticationProvider: Provedor de autenticacion propio de Spring boot no se tiene control sobre esta logica
    - AuthenticationManager:  Autenticado propio y ejecucion implicita de Spring boot no se tiene control sobre esta logica.


2. La clase UserDetailService es el que se encarga de obtener los datos de usuario de la base de datos y hacer el match o
validacion con la clave que vine de la peticion de auth o login este es el mecanismo implicito de spring security, esto lo ejecuta
implicitamente sring boot el proposito es traer la clave de la base de datos y compararla con la que vino de la peticion de auth o login
si coincide se crea el token de autenticacion y se retorna al cliente.


3. La clase FilterValidAuthPerRequest es el filter o middleware que se ejecuta en cada peticion y se valida si el usuario esta autenticado
si no esta autenticado se retorna un error 401. Aqui ya se valida el token si es valido cuando se hace peticiones de la ruta diferentes a login cuando no se tiene
la cabecera Authorization, cuando se tiene no se hace validacion del token, se esta en el proceso de autenticacion.
Ojo con esta condicion if (SecurityContextHolder.getContext().getAuthentication() == null) { es obligatorio cuando el token y las credenciales son validas se debe setear 
el SecurityContextHolder.getContext().setAuthentication con  la credenciles de auteticacion UsernamePasswordAuthenticationToken para que spring boot retorne correctamente
la peticion que se realizo y con la validacion ok del token y las credenciales.


4. Se puede crear un logica de autenticacion propia como CustomAuthenticationProvider que remplazaria a UserDetailService que se que dispara implicitamente spring boot.
Si se crea un custom AuthenticationProvider se debe configura o inscribir en WebSecurityConfig en la flag authenticationProvider y se replazaria por el propio de spring boot aqui ya no
es necesario los bean AuthenticationProvider y AuthenticationManager , esto custom validadadores de auth se usan cuando se tiene una logica de validacion especial y se necesita retonar otros valores diferentes
a la clase UserDetailService


5. La clase UsuarioModel es modelo que se usaria para recibir las credenciales de autenticacion y validarlas con la base de datos. es como un esquema estandar de spring boot


6. La clase JwtsUtils es la encargada de la logica de creacion del token una vez validado o realizado la autenticacion correctamente, esta clase se llama o se usa en el cotrolador de autenticacion o auth una vez eñ login sea exitoso
   se retorne el token y setambien se usa en la clase FilterValidAuthPerRequest cada vez que se haga un peticion en una ruta diferente a login y se valida si el token es el correcto.


7. Todo el flujo se dispara cuando se llama a la ruta /api//auth como el controlador de pruebas que se creo ControllerDummy y se ejecuta el metodo   authenticationManager.authenticate de la clase AuthenticationManager , aqui se dispara 
todo la logica de autenticacion ,validacion de credenciales y se retona el token.
