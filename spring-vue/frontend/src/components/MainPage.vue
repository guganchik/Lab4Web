<template>
  <div v-if="isAuthorized" class="list row">


    <div class="col-md-8">
      <div class="input-group mb-3"></div>
      <Form @submit="savePoint" :validation-schema="schema" >
      <div class="form-row">
        <div class="form-group col-md-4">
          <label for="X">X:</label>
          <Field name="fieldX" as="select" class="form-control" v-model="fieldX">
            <option value="-2">-2</option>
            <option value="-1.5">-1.5</option>
            <option value="-1">-1</option>
            <option value="-0.5">-0.5</option>
            <option value="0">0</option>
            <option value="0.5">0.5</option>
            <option value="1">1</option>
            <option value="1.5">1.5</option>
            <option value="2">2</option>
          </Field>
          <ErrorMessage name="fieldX" class="error-feedback" />
        </div>
        <div class="form-group col-md-4">
          <label for="Y">Y:</label>
          <Field name="fieldY" type="text" class="form-control"  v-model="fieldY"/>
          <!--<input type="text" class="form-control" v-model="fieldY">-->
          <ErrorMessage name="fieldY" class="error-feedback" />
        </div>
        <div class="form-group col-md-4">
          <label for="X">R:</label>
          <Field name="fieldR" as="select" class="form-control" v-model="fieldR" @change="changeRadius">
            <option value="-2">-2</option>
            <option value="-1.5">-1.5</option>
            <option value="-1">-1</option>
            <option value="-0.5">-0.5</option>
            <option value="0">0</option>
            <option value="0.5">0.5</option>
            <option value="1">1</option>
            <option value="1.5">1.5</option>
            <option value="2" selected>2</option>
          </Field>
          <ErrorMessage name="fieldR" class="error-feedback" />
        </div>
      </div>
      <div class="row">
        <div class="col-sm-12 text-center">
          <button id="btnSubmit" class="btn btn-primary btn-md center-block" Style="width: 100px; margin-right: 10px;" type="submit">Submit</button>
          <button id="btnClear" class="btn btn-danger btn-md center-block" Style="width: 100px; margin-left: 10px;" type="button" @click="clearPoints">Clear</button>
        </div>
      </div>
      </Form>

      <div class="input-group mb-3">
          <div id="jxgbox" class="jxgbox" style="width: 600px; height:600px;"></div>
      </div>

      <div class="input-group mb-3">
      </div>
    </div>

    <div class="col-md-8">
      <div class="container text-center">
        <div class="table-responsive my-5">
          <!-- The table component -->
          <Table :fields='fields' :items ="points"></Table>
        </div>
      </div>
    </div>
  </div>
</template>

<script>


import PointService from "../services/point.service";
import Table from './Table.vue';
import JXG from 'jsxgraph';
import { Form, Field, ErrorMessage } from "vee-validate";
import * as yup from "yup";

export default {
  name: "MainPage",
  computed: {
    isAuthorized() {
      return this.$store.state.auth.user;
    },
  },
  components: {
    Table,
    Form,
    Field,
    ErrorMessage,
  },
  data() {
    const schema = yup.object().shape({
      fieldX: yup
          .number()
          .min(-2, "Must be not lower than -2!")
          .max(2, "Must be not greater than 2!"),
      fieldY: yup
          .number()
          .min(-2.5, "Must be not lower than -2.5!")
          .max(2.5, "Must be not greater than 2.5!"),
      fieldR: yup
          .number()
          .min(0, "Must be not lower than 0!")
          .max(2, "Must be not grater than 2!"),
    });

    return {
      fieldX: 0,
      fieldY: 0,
      fieldR: 2,
      points: [],
      fields: [
        'id','x','y','r','hit', 'currentTime','workTime'
      ],
      schema,
    };
  },

  methods: {
    retrievePoints() {
      PointService.getAll()
        .then(response => {
          this.points = response.data;
          console.log(response.data);
          this.points.forEach((p) => {
            //const x = parseFloat(p.x);
            //const y = parseFloat(p.y);
            //const r = parseFloat(p.r);
            //const hit = true;
            window.area.put(p.x, p.y, p.r, p.hit);
          });
        })
        .catch(e => {
          console.log(e);
        });
    },

    savePoint() {
      var data = {
        x: this.fieldX,
        y: this.fieldY,
        r: this.fieldR
      };

      PointService.create(data)
          .then(response => {
            window.area.put(response.data.x, response.data.y, response.data.r, response.data.hit);
            this.points.push(response.data);
          })
          .catch(e => {
            console.log(e);
          });
    },

    clearPoints() {
      PointService.deleteAll()
          .then(response => {
            console.log(response.data);
            this.points = [];
            window.area.clearPoints();
          })
          .catch(e => {
            console.log(e);
          });
    },

    changeRadius() {
      if (this.fieldR < 0) {
        return;
      }
      window.area.changeRadius(this.fieldR);
    }
  },
  mounted() {
    this.retrievePoints();
    window.area = new Area(this.fieldR);
    window.area.board.on("down", async (e) => {
      if (this.fieldR < 0) {
        //alert("!!!");
        return;
      }

      let i;
      if (e[JXG.touchProperty]) {
        i = 0; // finger number
      }

      const [x, y] = window.area.getClickCoordinates(e, i);
      //console.log("x=" + x, " y=" + y, " r=" + this.fieldR);

      var data = {
        x: x,
        y: y,
        r: this.fieldR
      };
      PointService.create(data)
          .then(response => {
            window.area.put(response.data.x, response.data.y, response.data.r, response.data.hit);
            this.points.push(response.data);
          })
          .catch(e => {
            console.log(e);
          });
    });
  }
};

class Vertex {
  x;
  y;
  point;

  constructor(x, y, r, board) {
    this.x = x;
    this.y = y;
    this.point = board.create("point", [x * r, y * r]);
    this.point.hideElement();
  }

  rescale(r) {
    this.point.moveTo([this.x * r, this.y * r]);
  }
}

class Area {
  board;
  vertices;
  shapes;
  r;
  points;
  radius;

  static AREA_COLOR = "#ff6600";
  static POINT_HIT_COLOR = "#0f0";
  static POINT_MISS_COLOR = "#f00";

  constructor(r) {
    this.r = r;
    this.radius = r;
    this.initBoard();
    this.initVerticesAndDraw(r);
    this.points = [];
  }

  initBoard() {
    this.board = JXG.JSXGraph.initBoard('jxgbox', {
      axis: true,
      grid: false,
      boundingbox: [-2.5, 2.5, 2.5, -2.5],
      showNavigation: false,
      showCopyright: false,
      showZoom: false,
      defaultAxes: {
        x: { ticks: { visible: true, majorHeight: 5 } },
        y: { ticks: { visible: true, majorHeight: 5 } }
      }
    });
    JXG.GeometryElement.prototype.highlight = () => {};
  }

  initVerticesAndDraw(r) {
    const center = new Vertex(0, 0, r, this.board);
    const sectorVertices = [
      center,
      new Vertex(0, 0.5, r, this.board),
      new Vertex(-0.5, 0, r, this.board)
    ];
    const triangleVertices = [
      center,
      new Vertex(0.5, 0, r, this.board),
      new Vertex(0, -1, r, this.board)
    ];
    const rectangleVertices = [
      center,
      new Vertex(0, 1, r, this.board),
      new Vertex(1, 1, r, this.board),
      new Vertex(1, 0, r, this.board)
    ];

    const options = {
      fillColor: Area.AREA_COLOR,
      fillOpacity: .5,
      strokeWidth: 0
    };

    const jsxPoints = (vertices) => Array.from(vertices).map(each => each.point);

    const polygon = (vertices) => this.board.create("polygon", jsxPoints(vertices), options);
    this.board.create("sector", jsxPoints(sectorVertices), options);

    const borders = [
      ...polygon(triangleVertices).borders,
      ...polygon(rectangleVertices).borders
    ];

    Array.from(borders).forEach(b => b.hideElement());

    this.vertices = [...sectorVertices, ...triangleVertices, ...rectangleVertices];
  }


    rescale(r) {
    this.r = r;
    this.vertices.forEach(v => v.rescale(r));
  }

  changeRadius(radius) {
    this.radius = radius;
    this.rescale(radius);
  }


  put(x, y, r, hit) {
    const options = {
      fillColor: hit ? Area.POINT_HIT_COLOR : Area.POINT_MISS_COLOR,
      strokeColor: "#000",
      strokeWidth: 1,
      fixed: true,
      showInfobox: false
    };

    const hitStatus = hit ? "Hit" : "Miss";
    const point = this.board.create("point", [x, y], options);

    point.on("over", () => {
      //point.prevR = this.r;
      this.rescale(r)
      point.label.showElement();
    });
    point.on("out", () => {
      //this.rescale(point.prevR);
      this.rescale(this.radius);
      point.label.hideElement();



    });

    point.label.hideElement();
    point.label.setText(`${hitStatus} (${x.toFixed(2)}, ${y.toFixed(2)}, ${r.toFixed(2)})`);

    this.points.push(point);
  }
  getClickCoordinates(e, i) {
    const cPos = this.board.getCoordsTopLeftCorner(e, i);
    const absPos = JXG.getPosition(e, i);
    const dx = absPos[0] - cPos[0];
    const dy = absPos[1] - cPos[1];

    const calculatedCoordinates = new JXG.Coords(JXG.COORDS_BY_SCREEN, [dx, dy], this.board).usrCoords;
    return calculatedCoordinates.slice(1);
  }

  clearPoints() {
    this.points.forEach(p => this.board.removeObject(p));
  }
}

</script>

<style>
.list {
  text-align: left;
  max-width: 1200px;
  margin: auto;
}
</style>
