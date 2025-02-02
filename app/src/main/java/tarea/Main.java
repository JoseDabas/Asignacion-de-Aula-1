package tarea;

import io.javalin.Javalin;

public class Main {
    public static void main(String[] args) {
        var app = Javalin.create(javalinConfig -> {
            javalinConfig.staticFiles.add("/publico"); // Configurar la carpeta de archivos estáticos
        })
                .get("/", ctx -> {
                    Usuario usuario = ctx.sessionAttribute("usuario"); // Obtener el usuario de la sesión
                    if (usuario != null) {
                        ctx.html("<html><head><link rel='stylesheet' href='/styles.css'></head><body><h1>Bienvenido, "
                                + usuario.nombre() + "!</h1></body></html>"); // Mostrar el mensaje de bienvenida
                    } else {
                        ctx.redirect("/formulario.html"); // Redirigir al formulario si no está logueado
                    }
                })
                .start(7000); // Iniciar la aplicación en el puerto 7000

        app.before(ctx -> {
            System.out.println("Filtro de la barra..."); // Imprimir mensaje de filtro
            // Validacion del filtro
            Usuario usuario = ctx.sessionAttribute("usuario");

            if (usuario == null && !ctx.path().equals("/formulario.html") && !ctx.path().equals("/autenticar")) {
                // Redirigir al formulario si no está logueado y no está en el formulario o en
                // la rutas de autenticación
                ctx.redirect("/formulario.html");
            }
            System.out.println("Usuario: " + usuario); // Imprimir el usuario
        });

        app.post("/autenticar", ctx -> {
            String nombre = ctx.formParam("nombre");

            // Simulación de validación
            if (nombre != null && !nombre.isEmpty()) {
                Usuario usuario = new Usuario(nombre, "ICC", 1); // Crear un usuario
                ctx.sessionAttribute("usuario", usuario); // Guardar el usuario en la sesión
                ctx.redirect("/");
            } else {
                ctx.redirect("/formulario.html"); // Redirigir si la validación falla
            }
        }); // Autenticar el usuario
    }

    record Usuario(String nombre, String carrera, int id) {
    }
}
