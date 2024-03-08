import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';
import { ServicesDataService } from '../service/services-data.service';
import { Router } from '@angular/router';
import { ActivatedRoute } from '@angular/router';
@Component({
  selector: 'app-update',
  templateUrl: './update.component.html',
  styleUrls: ['./update.component.scss']
})
export class UpdateComponent implements OnInit {
  form: FormGroup = this.formBuilder.group({
    firstName: [''], // Inicializa con valores vacíos
    middleName: [''],
    lastName: [''],
    secondLastName: [''],
    birthDate: ['']
  });
  message: string = "";

  constructor(
    private formBuilder: FormBuilder,
    private servicesData: ServicesDataService,
    private router: Router,
    private route: ActivatedRoute
  ) { }
  cedula: string = "";
  ngOnInit(): void {
    this.route.queryParams.subscribe(params => {
      const cedula = params['cedula'];
      this.cedula = params['cedula'];
      if (cedula) {
        this.servicesData.getUserByUsername(cedula).subscribe(
          data => {
            if (data && data.data) {
              this.form.patchValue({
                firstName: data.data.firstName,
                middleName: data.data.middleName,
                lastName: data.data.lastName,
                secondLastName: data.data.secondLastName,
                birthDate: data.data.birthDate
              });
            }
          },
          error => {
            console.error('Error al obtener los datos del usuario', error);
          }
        );
      }
    });
  }

  onSubmit() {
    if (this.form.valid) {
      this.servicesData.setUserData(this.cedula, this.form.value).subscribe(
        response => {
          if (response.success === true) {
            // Redirige y muestra mensaje según la respuesta
            this.router.navigate(["list"]);
            this.message = response.data;
          } else {
            this.message = response.message;
          }
        },
        error => {
          console.error('Error al actualizar usuario', error);
        }
      );
    } else {
      console.log('Formulario no es válido');
    }
  }
}
