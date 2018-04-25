module.exports = function(app, jwt, gestorBD) {

    app.get("/api/amigos", function(req, res) {
        var usuario;
        var token = req.headers['token'];
        if (token != null) {
            // verificar el token
            jwt.verify(token, 'secreto', function (err, infoToken) {
                if (err) {
                    return;
                } else {
                    // dejamos correr la petición
                    usuario = infoToken.usuario;
                }
            });
        }
        var criterio = {"email" : usuario};
        gestorBD.obtenerUsuarios( criterio , function(usuarios) {
            if (usuarios == null) {
                res.status(500);
                res.json({
                    error : "se ha producido un error"
                })
            } else {
                res.status(200);
                res.send( JSON.stringify(usuarios[0].amigos) );
            }
        });
    });

    app.post("/api/autenticar/", function(req, res) {
        var seguro = app.get("crypto").createHmac('sha256', app.get('clave'))
            .update(req.body.password).digest('hex');
        var criterio = {
            email : req.body.email,
            password : seguro
        }

        gestorBD.obtenerUsuarios(criterio, function(usuarios) {
            if (usuarios == null || usuarios.length == 0) {
                res.status(401);
                res.json({
                    autenticado : false
                })
            } else {
                var token = app.get('jwt').sign(
                    {usuario: criterio.email , tiempo: Date.now()/1000},
                    "secreto");
                res.status(200);
                res.json({
                    autenticado: true,
                    token : token
                });
            }

        });
    });

    app.put("/api/amigos/mensaje", function(req, res) {
        var usuario;
        var token = req.headers['token'];
        if (token != null) {
            // verificar el token
            jwt.verify(token, 'secreto', function (err, infoToken) {
                if (err) {
                    return;
                } else {
                    // dejamos correr la petición
                    usuario = infoToken.usuario;
                }
            });
        }
        var criterio = {"email" : usuario};
        gestorBD.obtenerUsuarios( criterio , function(usuarios) {
            if (usuarios == null) {
                res.status(500);
                res.json({
                    error : "se ha producido un error"
                })
            } else {
                res.status(200);
                res.send( JSON.stringify(usuarios[0].amigos) );
            }
        });
    });

}