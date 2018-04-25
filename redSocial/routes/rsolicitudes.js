module.exports = function (app, swig, gestorBD) {

    app.get('/solicitud/:id', function (req, res) {
        var criterio = {"_id": gestorBD.mongo.ObjectID(req.params.id)};
        gestorBD.enviarSolicitud(criterio, req.session.usuario, function (id) {
            if (id == null) {
                res.redirect("/usuarios?mensaje=Fallo al enviar la solicitud" +
                    "&tipoMensaje=alert-danger");
            } else {
                res.redirect("/usuarios?mensaje=Solicidud enviada con exito" +
                    "&tipoMensaje=alert-success");
            }
        });
    });

    app.get("/solicitudes", function (req, res) {
        var criterio = {"email": req.session.usuario};

        var pg = parseInt(req.query.pg); // Es String !!!
        if (req.query.pg == null) { // Puede no venir el param
            pg = 1;
        }

        gestorBD.obtenerUsuarios(criterio, function (usuarios) {
            if (usuarios == null) {
                res.send("Error al listar ");
            } else {
                var solicitudes = usuarios[0].solicitudes;

                if (solicitudes != null) {
                    if (solicitudes.length >= (pg - 1) * 5 + 5) {
                        solicitudes = solicitudes.slice((pg - 1) * 5, (pg - 1) * 5 + 5);
                    } else {
                        solicitudes = solicitudes.slice((pg - 1) * 5);
                    }

                    var total = solicitudes.length;
                    var pgUltima = total / 4;
                    if (total % 4 > 0) { // Sobran decimales
                        pgUltima = Math.trunc(pgUltima + 1);
                    }


                    var criterio = {"email": {$in: solicitudes.toString().split(',')}};
                    gestorBD.obtenerUsuarios(criterio, function (usuarios) {
                        if (usuarios == null) {
                            res.redirect("/usuarios?mensaje=Fallo al listar solicitudes" +
                                "&tipoMensaje=alert-danger");
                        } else {
                            var respuesta = swig.renderFile('views/bsolicitudes.html',
                                {
                                    solicitudes: usuarios,
                                    pgActual: pg,
                                    pgUltima: pgUltima
                                });
                            res.send(respuesta);
                        }
                    });
                } else {
                    var respuesta = swig.renderFile('views/bsolicitudes.html',
                        {
                            solicitudes: null,
                            pgActual: 1,
                            pgUltima: 1
                        });
                    res.send(respuesta);
                }

            }
        });
    });

}