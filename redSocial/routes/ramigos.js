module.exports = function (app, swig, gestorBD) {

    app.get('/aceptar/:id', function (req, res) {

        var criterio = {"_id": gestorBD.mongo.ObjectID(req.params.id)};

        gestorBD.agregarAmigo(criterio, req.session.usuario, function (id) {
            if (id == null) {
                res.redirect("/solicitudes?mensaje=Fallo al aceptar la solicitud" +
                    "&tipoMensaje=alert-danger");
            } else {
                gestorBD.obtenerUsuarios(criterio, function (usuarios) {
                    if (usuarios == null) {
                        res.redirect("/solicitudes?mensaje=Fallo al aceptar la solicitud" +
                            "&tipoMensaje=alert-danger");
                    } else {
                        criterio = {"email": req.session.usuario};
                        gestorBD.agregarAmigo(criterio, usuarios[0].email, function (id) {
                            if (id == null) {
                                res.redirect("/solicitudes?mensaje=Fallo al aceptar la solicitud" +
                                    "&tipoMensaje=alert-danger");
                            } else {
                                gestorBD.aceptarSolicitud(criterio, usuarios[0].email, function (id) {
                                    if (id == null) {
                                        res.redirect("/solicitudes?mensaje=Fallo al aceptar la solicitud" +
                                            "&tipoMensaje=alert-danger");
                                    } else {
                                        res.redirect("/solicitudes?mensaje=Solicitud aceptada" +
                                            "&tipoMensaje=alert-success");
                                    }
                                });
                            }
                        });
                    }
                });
            }
        });

    });

    app.get("/amigos", function (req, res) {
        var criterio = {"email": req.session.usuario};

        var pg = parseInt(req.query.pg); // Es String !!!
        if (req.query.pg == null) { // Puede no venir el param
            pg = 1;
        }

        gestorBD.obtenerUsuarios(criterio, function (usuarios) {
            if (usuarios == null) {
                res.send("Error al listar ");
            } else {
                var amigos = usuarios[0].amigos;

                if (amigos != null) {
                    if (amigos.length >= (pg - 1) * 5 + 5) {
                        amigos = amigos.slice((pg - 1) * 5, (pg - 1) * 5 + 5);
                    } else {
                        amigos = amigos.slice((pg - 1) * 5);
                    }

                    var total = amigos.length;
                    var pgUltima = total / 4;
                    if (total % 4 > 0) { // Sobran decimales
                        pgUltima = Math.trunc(pgUltima + 1);
                    }


                var criterio = { "email" : { $in: amigos.toString().split(',') } };
                gestorBD.obtenerUsuarios(criterio, function (usuarios) {
                    if (usuarios == null || usuarios.length <= 0) {
                        var respuesta = swig.renderFile('views/bamigos.html',
                            {
                                amigos: null,
                                pgActual: pg,
                                pgUltima: pgUltima
                            });
                        res.send(respuesta);
                    } else {
                        var respuesta = swig.renderFile('views/bamigos.html',
                            {
                                amigos: usuarios,
                                pgActual: pg,
                                pgUltima: pgUltima
                            });
                        res.send(respuesta);
                    }
                });

            } else {
                var respuesta = swig.renderFile('views/bamigos.html',
                    {
                        amigos: null,
                        pgActual: 1,
                        pgUltima: 1
                    });
                res.send(respuesta);
            }
            }
        });
    });
}