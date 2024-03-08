import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';
import { ServicesDataService } from '../service/services-data.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.scss']
})
export class RegisterComponent implements OnInit {

  constructor(private formBuilder: FormBuilder, private servicesData: ServicesDataService,private router:Router) { }

  form: FormGroup = new FormGroup({});
  message: string = "";
  ngOnInit(): void {
    this.form = this.formBuilder.group({
      username: ['cedula'], // Valor predeterminado, puedes dejarlo vacío si lo prefieres
      password: ['12345678'], // Valor predeterminado, ajusta según necesites
      firstName: ['Nombre'], // Valor predeterminado
      middleName: [''], // Valor predeterminado
      lastName: ['Apellido'], // Valor predeterminado
      secondLastName: [''], // Valor predeterminado
      birthDate: ['2024-03-7'] 
    });
  }
  onSubmit() {
    this.servicesData.setNewUser(this.form.value).subscribe(
      response => {
      if(response["success"]==true){
        this.router.navigate(["list"])
        this.message = response["data"]
      }else{
        this.message = response["message"]
      }
      console.log(response);
      }
    )
  }
}
