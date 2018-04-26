module.exports = function (app, swig, gestorBD) {

    app.post('/usuario', function (req, res) {
        if (req.body.password != req.body.confpassword) {
            res.redirect("/registrarse?mensaje=Las contraseñas deben coincidir" +
                "&tipoMensaje=alert-danger ");
            return;
        }

        var seguro = app.get("crypto").createHmac('sha256', app.get('clave'))
            .update(req.body.password).digest('hex');
        var usuario = {
            email: req.body.email,
            name : req.body.name,
            password: seguro
        }

        var usuarioNombre = {
            email: req.body.email
        }

        gestorBD.obtenerUsuarios(usuarioNombre, function (usuarios) {
            if (usuarios == null || usuarios.length == 0) {
                gestorBD.insertarUsuario(usuario, function (id) {
                    if (id == null) {
                        res.redirect("/registrarse?mensaje=Error al registrar usuario" +
                            "&tipoMensaje=alert-danger ");
                    } else {
                        res.redirect("/identificarse?mensaje=Nuevo usuario registrado" +
                            "&tipoMensaje=alert-success");
                    }
                });
            } else {
                res.redirect("/registrarse?mensaje=Ya existe un usuario con este Email" +
                    "&tipoMensaje=alert-danger ");
            }
        });

    })

    app.get("/registrarse", function (req, res) {
        var respuesta = swig.renderFile('views/bregistro.html', {});
        res.send(respuesta);
    });

    app.post("/identificarse", function (req, res) {
        var seguro = app.get("crypto").createHmac('sha256', app.get('clave'))
            .update(req.body.password).digest('hex');
        var criterio = {
            email: req.body.email,
            password: seguro
        }
        gestorBD.obtenerUsuarios(criterio, function (usuarios) {
            if (usuarios == null || usuarios.length == 0) {
                req.session.usuario = null;
                usuarioActivo = false;
                res.redirect("/identificarse?mensaje=Error email o contraseña incorrectas" +
                    "&tipoMensaje=alert-danger ");
            } else {
                req.session.usuario = usuarios[0].email;
                usuarioActivo = true;
                res.redirect("/usuarios?mensaje=Bienvenido "+ usuarios[0].email +
                    "&tipoMensaje=alert-success");
            }
        });
    });

    app.get("/identificarse", function (req, res) {
        var respuesta = swig.renderFile('views/bidentificacion.html', {});
        res.send(respuesta);
    });

    app.get('/desconectarse', function (req, res) {
        req.session.usuario = null;
        usuarioActivo = false;
        res.redirect("/identificarse?mensaje=Usuario desconectado" +
            "&tipoMensaje=alert-success");require("./routes/rapicanciones.js")(app, gestorBD);
    })

    app.get("/usuarios", function (req, res) {
        var criterio = {};
        if (req.query.busqueda != null) {
            criterio = {$or: [{"name": {$regex: ".*" + req.query.busqueda + ".*"}}, {"email": {$regex: ".*" + req.query.busqueda + ".*"}}]};
        }

        var pg = parseInt(req.query.pg); // Es String !!!
        if ( req.query.pg == null){ // Puede no venir el param
            pg = 1;
        }

        gestorBD.obtenerUsuariosPg(criterio, pg , function(usuarios, total ) {
            if (usuarios == null) {
                res.send("Error al listar ");
            } else {
                console.log(usuarios);

                var pgUltima = total/5;
                if (total % 5 > 0 ){ // Sobran decimales
                    pgUltima = Math.trunc(pgUltima+1);
                }

                var respuesta = swig.renderFile('views/busuarios.html',
                    {
                        usuarioActual: req.session.usuario,
                        usuarios : usuarios,
                        pgActual : pg,
                        pgUltima : pgUltima
                    });
                res.send(respuesta);
            }
        });

    });
};