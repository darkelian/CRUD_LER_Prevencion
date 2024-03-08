import { Component } from '@angular/core';
import { ServicesDataService } from '../service/services-data.service';
import { Router } from '@angular/router';
@Component({
  selector: 'app-layout',
  templateUrl: './layout.component.html',
  styleUrls: ['./layout.component.scss']
})
export class LayoutComponent {
  data: any = null;

  constructor(private router: Router, private servicesData: ServicesDataService) { }
  message: string = "";
  ngOnInit() {
    this.loadAllData();
  }
  dateUpdate(state: boolean) {
    this.servicesData.getDataFilter(state).subscribe(
      data => {
        this.data = data;
      }
    )
  }
  dateDelete(state: string) {
    this.servicesData.deleteUser(state).subscribe(
      data => {
        this.data = data;
        if (data["success"] == true) {
          this.message = data["data"]
          this.servicesData.getDataTable().subscribe(
            data => {
              this.data = data;
            }
          )
        } else {
          this.message = data["message"]
        }
        console.log(data);
      }
    )
  }
  navigateToUpdate(cedula: string) {
    this.router.navigate(['/update'], { queryParams: { cedula: cedula } });
  }
  onSearch(term: string) {
    if (term) {
      this.servicesData.getSearch(term).subscribe(
        data => {
          this.data = data;
        },
        error => {
          console.error('Error al obtener los datos de búsqueda', error);
          this.message = "Error al realizar la búsqueda";
        }
      );
    } else {
      // Si el término de búsqueda está vacío, recargar todos los datos
      this.loadAllData();
    }
  }
  loadAllData() {
    this.servicesData.getDataTable().subscribe(
      data => {
        this.data = data;
      },
      error => {
        console.error('Error al obtener todos los datos', error);
        this.message = "Error al cargar los datos";
      }
    );
  }
}
