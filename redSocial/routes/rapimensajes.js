module.exports = function (app, jwt, gestorBD) {

    app.get("/api/usuarios", function (req, res) {
        var criterio = {"email": res.usuario};
        gestorBD.obtenerUsuarios(criterio, function (usuarios) {
            if (usuarios == null) {
                res.status(500);
                res.json({
                    error: "se ha producido un error"
                })
            } else {
                res.status(200);
                res.send(JSON.stringify(usuarios[0].amigos));
            }
        });
    });

    app.post("/api/autenticar/", function (req, res) {
        var seguro = app.get("crypto").createHmac('sha256', app.get('clave'))
            .update(req.body.password).digest('hex');
        var criterio = {
            email: req.body.email,
            password: seguro
        }

        gestorBD.obtenerUsuarios(criterio, function (usuarios) {
            if (usuarios == null || usuarios.length == 0) {
                res.status(401);
                res.json({
                    autenticado: false
                })
            } else {
                var token = app.get('jwt').sign(
                    {usuario: criterio.email, tiempo: Date.now() / 1000},
                    "secreto");
                res.status(200);
                res.json({
                    autenticado: true,
                    token: token
                });
            }

        });
    });

    app.post("/api/mensaje", function (req, res) {
        var destino = req.body.destino
        var mensaje = {
            emisor: res.usuario,
            destino: destino,
            texto: req.body.texto,
            leido: false
        }
        var criterio = {"email": res.usuario};

        gestorBD.obtenerUsuarios(criterio, function (usuarios) {
            if (usuarios == null) {
                res.status(500);
                res.json({
                    error: "se ha producido un error"
                })
            } else {
                if (usuarios[0].amigos.indexOf(destino) != -1) {

                    gestorBD.enviarMensaje(mensaje, function (id) {
                        if (id == null) {
                            res.status(500);
                            res.json({
                                error: "se ha producido un error"
                            })
                        } else {
                            res.status(200);
                            res.send(JSON.stringify(mensaje));
                        }
                    });
                } else {
                    res.status(500);
                    res.json({
                        error: "Solo se puede enviar un mensaje a un amigo"
                    })
                }
            }
        });

    });

    app.get("/api/mensajes/:usuario2", function (req, res) {

        var criterio = { $or : [
                { $and : [ { emisor : res.usuario }, { destino : req.params.usuario2 } ] },
                { $and : [ { emisor : req.params.usuario2 }, { destino : res.usuario } ] }
            ]};

        gestorBD.obtenerMensajes(criterio, function (mensajes) {
            if (mensajes == null) {
                res.status(500);
                res.json({
                    error: "se ha producido un error"
                })
            } else {
                res.status(200);
                res.send(JSON.stringify(mensajes));

            }
        });

    });

    app.put("/api/mensaje/:email", function (req, res) {
        var criterio = {"email": req.params.email};

        gestorBD.obtenerMensajes(criterio, function (mensajes) {
            if (mensajes == null) {
                res.status(500);
                res.json({
                    error: "se ha producido un error"
                })
            } else {
                var mensaje = mensajes[0];
                if(mensaje.destino == res.usuario) {
                    gestorBD.leerMensaje(criterio, function (id) {
                        if (id == null) {
                            res.status(500);
                            res.json({
                                error: "se ha producido un error"
                            })
                        } else {
                            res.status(200);
                            res.send(JSON.stringify(mensaje));
                        }
                    })
                }else{
                    res.status(500);
                    res.json({
                        error: "No se puede leer un mensaje que no es para ti"
                    })
                }
            }
        });

    });

}