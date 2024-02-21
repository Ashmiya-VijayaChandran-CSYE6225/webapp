packer {
  required_plugins {
    googlecompute = {
      source  = "github.com/hashicorp/googlecompute"
      version = "~> 1"
    }
  }
}

// variable "gcp_project_id" {
//   description = "Your GCP Project ID"
// }

// variable "gcp_zone" {
//   description = "Your GCP Default Zone"
// }

// variable "private_key_file" {
//   description = "Path to your GCP service account private key file"
// }

// locals {
//   image_description = "Custom image with CentOS Stream 8, Java, MySQL, and Java application"
// }

source "googlecompute" "centos8_image" {
  project_id          = "csye6225-dev-123"
  source_image_family = "centos-stream-8"
  image_name          = "centos-custom-image-google"
  image_family        = "custom-image"
  zone                = "us-east1-c"
  ssh_username        = "packer"
  #  private_key_file  = var.private_key_file
  image_description     = "custom-image-description"
  credentials_file      = "C:/Users/ashmi/Documents/CSYE6225/csye6225-dev-123-95776d3a0733.json"
  service_account_email = "csye6225-packer@csye6225-dev-123.iam.gserviceaccount.com"
  #  use_internal_ip   = false
  #  communicator      = "ssh"
}

build {
  sources = ["source.googlecompute.centos8_image"]

  provisioner "shell" {
    script = "script/installation.sh"
  }
  provisioner "shell" {
    script = "script/user.sh"
  }
  provisioner "file" {
    source      = "../target/webapp-1.0-SNAPSHOT.jar"
    destination = "/tmp/webapp-1.0-SNAPSHOT.jar"
  }
  provisioner "shell" {
    script = "script/chown.sh"
  }

  post-processor "googlecompute-export" {
    // googlecompute {
    type          = "googlecompute"
    project_id    = "csye6225-dev-123"
    image_name    = "exported-centos-custom-image-google"
    source_image  = "centos-stream-8"
    zone          = "us-central1-a"
    image_family  = "exported-centos-stream-8"
    json_key_file = "C:/Users/ashmi/Documents/CSYE6225/csye6225-dev-123-95776d3a0733.json"
  }
}