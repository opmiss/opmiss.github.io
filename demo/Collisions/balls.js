 PhiloGL.Shaders.Fragment.Ufm = [

    "#ifdef GL_ES",
    "precision highp float;",
    "#endif",

    "varying vec4 vColor;",
    "varying vec2 vTexCoord;",
    "varying vec3 lightWeighting;",

    "uniform bool hasTexture1;",
    "uniform sampler2D sampler1;",

    "uniform bool enablePicking;",
    "uniform vec3 pickColor;",

    "uniform bool hasFog;",
    "uniform vec3 fogColor;",

    "uniform float fogNear;",
    "uniform float fogFar;",

    "uniform vec4 colorUfm;",

    "void main(){",

      "if(!hasTexture1) {",
        "gl_FragColor = vec4(colorUfm.rgb * lightWeighting, colorUfm.a);",
      "} else {",
        "gl_FragColor = vec4(texture2D(sampler1, vec2(vTexCoord.s, vTexCoord.t)).rgb * lightWeighting, 1.0);",
      "}",

      "if(enablePicking) {",
        "gl_FragColor = vec4(pickColor, 1.0);",
      "}",

      /* handle fog */
      "if (hasFog && colorUfm.r != 1.0) {",
        "float depth = gl_FragCoord.z / gl_FragCoord.w;",
        "float fogFactor = smoothstep(fogNear, fogFar, depth);",
        "gl_FragColor = mix(gl_FragColor, vec4(fogColor, gl_FragColor.w), fogFactor);",
      "}",

    "}"

  ].join("\n");

(function() {
  //Unpack PhiloGL modules
  PhiloGL.unpack();

  //Utility fn to getElementById
  function $id(d) {
    return document.getElementById(d);
  }

  var models = [], i = 50;
  while (i--) {
    var model = new O3D["Sphere"]({
      pickable: true,
      shininess: 2,
      radius: 2,
      colors: [0.5, 0.5, 0.5, 1],
      uniforms: {
        'colorUfm': [0.5, 0.5, 0.5, 1]
      }
    });
    var n = 20;
    model.position = {
      x: (i%10-4.5)*5,
      y: Math.floor(i/10-2)*5,
      z: 10
    };
    model.velocity = {
      x: Math.random()/2,
      y: Math.random()/2,
      z: Math.random()/2
    }
    model.update();
    models.push(model);
    model.texCoords = false;
  }

  function scale(v, s){
    v.x*=s; v.y*=s; v.z*=s; return v;
  }

  function makeScaled(v, s){
    var m = {}; m.x=v.x*s; m.y=v.y*s; m.z=v.z*s;
    return m;
  }

  function plus(v, dv){
    v.x+=dv.x; v.y+=dv.y; v.z+=dv.z; return v;
  }
  function subtract(v, dv){
    v.x-=dv.x; v.y-=dv.y; v.z-=dv.z; return v;
  }
  function vec(p1, p2){
    var v={}; v.x = p2.x-p1.x; v.y = p2.y-p1.y; v.z = p2.z-p1.z; return v;
  }
  function dot(v1, v2){
    var d=0; d+=v1.x*v2.x; d+=v1.y*v2.y; d+=v1.z*v2.z; return d;
  }
  function n2(v){
    return v.x*v.x+v.y*v.y+v.z*v.z;
  }
  function update(){
    var pij, vij, d2, d, x, t;
    for (var i=0; i<models.length-1; i++){
      for (var j=i+1; j<models.length; j++){
        pij = vec(models[i].position, models[j].position);
        vij = vec(models[i].velocity, models[j].velocity);
        d = dot(pij, vij);
        d2 = n2(pij);
        if (d2<16&&d<0){
          //  d = Math.sqrt(d2);
            pij = scale(pij, 1/Math.sqrt(d2));
            var vi = makeScaled(pij, dot(models[i].velocity, pij));
            var vj = makeScaled(pij, dot(models[j].velocity, pij));
            models[i].velocity = plus(subtract(models[i].velocity, vi), vj);
            models[j].velocity = plus(subtract(models[j].velocity, vj), vi);
        }
      }
    }
    for (var i=0; i<models.length; i++){
      if ((models[i].position.x<-24 && models[i].velocity.x<0)||(models[i].position.x>24 && models[i].velocity.x>0)){
          models[i].velocity.x=-models[i].velocity.x;
      }
      if ((models[i].position.y<-10 && models[i].velocity.y<0)||(models[i].position.y>10 && models[i].velocity.y>0)){
          models[i].velocity.y=-models[i].velocity.y;
      }
      if ((models[i].position.z<0 && models[i].velocity.z<0)||(models[i].position.z>20 && models[i].velocity.z>0)){
          models[i].velocity.z=-models[i].velocity.z;
      }
      models[i].position = plus(models[i].position, models[i].velocity);
      models[i].velocity = scale(models[i].velocity, 0.999);
      models[i].update();
    }
  }
  window.init = function() {
    // var stats = new xStats();
    // document.body.appendChild(stats.element);

    //Create App
    PhiloGL('surface-explorer-canvas', {
      program: {
        fs: 'Ufm'
      },
      camera: {
        position: {
          x: 0, y: 0, z: -30
        }
      },
      scene: {
        lights: {
          enable: true,
          ambient: {
            r: 0.6,
            g: 0.6,
            b: 0.6
          },
          directional: {
            color: {
              r: 0.8, g: 0.8, b: 0.8
            },
            direction: {
              x: 0, y: 0, z: -1
            }
          }
        },
        effects: {
          fog: {
            color: {
              r: 0.1, g: 0.1, b: 0.1
            },
            near: 0,
            far: 70
          }
        }
      },
      events: {
        picking: true,
        // lazyPicking: true,
        // centerOrigin: false,
        onMouseEnter: function(e, model) {
          model.uniforms.colorUfm = [0.5, 1, 0.8, 1];
        },
        onMouseLeave: function(e, model) {
          model.uniforms.colorUfm = [0.5, 0.5, 0.5, 1];
        }
        //onClick: function(e, model){
          //model.velocity.x+=Math.random();
        //}
      },
      onError: function() {
        alert("There was an error while creating the WebGL application");
      },
      onLoad: function(app) {
        var gl = app.gl,
            canvas = gl.canvas,
            scene = app.scene;
        //Basic gl setup
        gl.clearDepth(1.0);
        gl.clearColor(0, 0, 0, 1.0);
        gl.enable(gl.DEPTH_TEST);
        gl.depthFunc(gl.LEQUAL);
        //Add balls
        scene.add.apply(scene, models);
        //run loop
        render();
        //Render the scene and perform a new loop
        function render() {
          gl.viewport(0, 0, canvas.width, canvas.height);
          gl.clear(gl.COLOR_BUFFER_BIT | gl.DEPTH_BUFFER_BIT);
          update();
          //models[0].position.x ++;
          //models[0].update();
          scene.render();
          Fx.requestAnimationFrame(render);
        }
      }
    });
  };
})();
