# Proyecto de Spring security para autenticacion

### Logica de ejecucion

1. Se crea la clase de configuración WebSecurityConfig el método SecurityFilterChain de esta clase es solo configuración de spring security y beans para hacer funcionar el proceso implícitamente , se crean los beans:

    PasswordEncoder: codificación de la clave y validación con Bycript
    AuthenticationProvider: proveedor de autenticación propio de Spring boot no se tiene control sobre esta logica
    AuthenticationManager:  Autenticador propio y ejecución implícita de Spring boot no se tiene control sobre esta lógica.

2. La clase UserDetailService es el que se encarga de obtener los datos de usuario de la base de datos y hacer el match o validación con la clave que vine de la petición de auth o login este es el mecanismo implícito de spring security, esto lo ejecuta implícitamente sring boot el propósito es traer la clave de la base de datos y compararla con la que vino de la petición de auth o login si coincide se crea el token de autenticación y se retorna al cliente.

3. La clase FilterValidAuthPerRequest es el filter o middleware que se ejecuta en cada petición y se valida si el usuario está autenticado si no esta autenticado, se retorna un error 401. Aqui ya se valida el token, si es válido cuando se hace peticiones de la ruta diferentes a login cuando no se tiene la cabecera Authorization, cuando se tiene no se hace validacion del token, se esta en el proceso de autenticación. Ojo con esta condición if (SecurityContextHolder.getContext().getAuthentication() == null) { es obligatorio cuando el token y las credenciales son validas se debe setear el SecurityContextHolder.getContext().setAuthentication con la credenciales de autenticación UsernamePasswordAuthenticationToken para que spring boot retorne correctamente la petición que se realizó y con la validación ok del token y las credenciales.

4. Se puede crear un lógica de autenticación propia como CustomAuthenticationProvider que remplazaría a UserDetailService que se que dispara implicitamente spring boot. Si se crea un custom AuthenticationProvider se debe configurar o inscribir en WebSecurityConfig en la flag authenticationProvider y se replazaria por el propio de spring boot aqui ya no es necesario los bean AuthenticationProvider y AuthenticationManager , esto custom validadadores de auth se usan cuando se tiene una lógica de validación especial y se necesita retonar otros valores diferentes a la clase UserDetailService


5. La clase UsuarioModel es modelo que se usaría para recibir las credenciales de autenticación y validarlas con la base de datos. es como un esquema estándar de spring boot


6. La clase JwtsUtils es la encargada de la lógica de creación del token una vez validado o realizado la autenticación correctamente, esta clase se llama o se usa en el cotrolador de autenticación o auth una vez eñ login sea exitoso
   se retorne el token y también se usa en la clase FilterValidAuthPerRequest cada vez que se haga una petición en una ruta diferente al login y se valida si el token es el correcto.


7. Todo el flujo se dispara cuando se llama a la ruta /api//auth como el controlador de pruebas que se creó ControllerDummy y se ejecuta el método   authenticationManager.authenticate de la clase AuthenticationManager , aquí se dispara 
toda la lógica de autenticacion , validación de credenciales y se retorna el token.
